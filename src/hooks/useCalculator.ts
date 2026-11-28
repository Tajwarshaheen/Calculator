import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { CalculatorEngine } from '../engine/CalculatorEngine';
import { NumberFormatter } from '../engine/NumberFormatter';
import type {
  AngleMode,
  CalcError,
  CalculatorState,
  HistoryItem,
  ThemeMode,
  UserPreferences,
} from '../types/calculator';

const PREFS_STORAGE_KEY = 'taju_settings';
const HISTORY_STORAGE_KEY = 'taju_calculation_history';

const DEFAULT_PREFS: UserPreferences = {
  themeMode: 'DARK',
  angleMode: 'DEG',
  thousandsSeparator: true,
  hapticFeedback: true,
  memoryValue: 0,
};

function loadPreferences(): UserPreferences {
  try {
    const raw = localStorage.getItem(PREFS_STORAGE_KEY);
    if (!raw) return DEFAULT_PREFS;
    const parsed = JSON.parse(raw) as Partial<UserPreferences>;
    return {
      themeMode:
        parsed.themeMode === 'DARK' ||
        parsed.themeMode === 'LIGHT' ||
        parsed.themeMode === 'SYSTEM'
          ? parsed.themeMode
          : 'DARK',
      angleMode:
        parsed.angleMode === 'DEG' || parsed.angleMode === 'RAD'
          ? parsed.angleMode
          : 'DEG',
      thousandsSeparator:
        typeof parsed.thousandsSeparator === 'boolean'
          ? parsed.thousandsSeparator
          : true,
      hapticFeedback:
        typeof parsed.hapticFeedback === 'boolean'
          ? parsed.hapticFeedback
          : true,
      memoryValue:
        typeof parsed.memoryValue === 'number' &&
        Number.isFinite(parsed.memoryValue)
          ? parsed.memoryValue
          : 0,
    };
  } catch {
    return DEFAULT_PREFS;
  }
}

function loadHistory(): HistoryItem[] {
  try {
    const raw = localStorage.getItem(HISTORY_STORAGE_KEY);
    if (!raw) return [];
    const parsed = JSON.parse(raw);
    if (!Array.isArray(parsed)) return [];
    return parsed.filter(
      (item): item is HistoryItem =>
        Boolean(item) &&
        typeof item.id === 'string' &&
        typeof item.expression === 'string' &&
        typeof item.rawResult === 'string',
    );
  } catch {
    return [];
  }
}

function isBinaryOperator(ch: string): boolean {
  return (
    ch === '+' ||
    ch === '−' ||
    ch === '-' ||
    ch === '×' ||
    ch === '*' ||
    ch === '÷' ||
    ch === '/' ||
    ch === '^'
  );
}

function isDigitChar(ch: string): boolean {
  return ch >= '0' && ch <= '9';
}

interface InternalCalcState {
  expression: string;
  lastEvaluatedExpression: string;
  error: CalcError | null;
  justEvaluated: boolean;
  isInverseTrig: boolean;
}

const INITIAL_CALC_STATE: InternalCalcState = {
  expression: '',
  lastEvaluatedExpression: '',
  error: null,
  justEvaluated: false,
  isInverseTrig: false,
};

export function useCalculator() {
  const engineRef = useRef<CalculatorEngine>(new CalculatorEngine());
  const [internal, setInternal] = useState<InternalCalcState>(INITIAL_CALC_STATE);
  const [preferences, setPreferences] = useState<UserPreferences>(loadPreferences);
  const [history, setHistory] = useState<HistoryItem[]>(loadHistory);

  useEffect(() => {
    try {
      localStorage.setItem(PREFS_STORAGE_KEY, JSON.stringify(preferences));
    } catch {
      // Ignore storage quota errors
    }
  }, [preferences]);

  useEffect(() => {
    try {
      localStorage.setItem(HISTORY_STORAGE_KEY, JSON.stringify(history));
    } catch {
      // Ignore storage quota errors
    }
  }, [history]);

  // Apply light/dark theme to <html>
  useEffect(() => {
    const media = window.matchMedia('(prefers-color-scheme: dark)');
    const applyTheme = () => {
      const useDark =
        preferences.themeMode === 'DARK' ||
        (preferences.themeMode === 'SYSTEM' && media.matches);
      document.documentElement.classList.toggle('dark', useDark);
    };
    applyTheme();
    media.addEventListener('change', applyTheme);
    return () => media.removeEventListener('change', applyTheme);
  }, [preferences.themeMode]);

  const previewResult = useMemo(() => {
    const expr = internal.expression;
    if (!expr || internal.justEvaluated) return '';
    const hasOperation =
      /[+−\-×*÷/^%!()πe√]/.test(expr) ||
      expr.includes('sin') ||
      expr.includes('cos') ||
      expr.includes('tan') ||
      expr.includes('log') ||
      expr.includes('ln');
    if (!hasOperation) return '';
    const res = engineRef.current.evaluate(expr, preferences.angleMode, true);
    return res.ok ? res.rawString : '';
  }, [internal.expression, internal.justEvaluated, preferences.angleMode]);

  const state: CalculatorState = useMemo(
    () => ({
      expression: internal.expression,
      previewResult,
      lastEvaluatedExpression: internal.lastEvaluatedExpression,
      error: internal.error,
      justEvaluated: internal.justEvaluated,
      isInverseTrig: internal.isInverseTrig,
      preferences,
      history,
    }),
    [internal, previewResult, preferences, history],
  );

  const formattedExpression = useMemo(
    () =>
      !state.expression
        ? '0'
        : NumberFormatter.formatExpression(
            state.expression,
            preferences.thousandsSeparator,
          ),
    [state.expression, preferences.thousandsSeparator],
  );

  const formattedPreview = useMemo(
    () =>
      !state.previewResult
        ? ''
        : NumberFormatter.formatResult(
            state.previewResult,
            preferences.thousandsSeparator,
          ),
    [state.previewResult, preferences.thousandsSeparator],
  );

  const formattedMemory = useMemo(
    () =>
      NumberFormatter.formatDouble(
        preferences.memoryValue,
        preferences.thousandsSeparator,
      ),
    [preferences.memoryValue, preferences.thousandsSeparator],
  );

  const hasMemory = preferences.memoryValue !== 0;

  const triggerHaptic = useCallback(() => {
    if (preferences.hapticFeedback && typeof navigator !== 'undefined' && navigator.vibrate) {
      navigator.vibrate(8);
    }
  }, [preferences.hapticFeedback]);

  const onDigit = useCallback((digit: string) => {
    setInternal((prev) => {
      const base = prev.justEvaluated ? '' : prev.expression;
      const next =
        base === '0' && digit !== '0'
          ? digit
          : base === '0' && digit === '0'
            ? '0'
            : base + digit;
      return {
        ...prev,
        expression: next,
        lastEvaluatedExpression: prev.justEvaluated
          ? ''
          : prev.lastEvaluatedExpression,
        error: null,
        justEvaluated: false,
      };
    });
  }, []);

  const onDecimal = useCallback(() => {
    setInternal((prev) => {
      const base = prev.justEvaluated ? '' : prev.expression;
      if (!base) {
        return {
          ...prev,
          expression: '0.',
          lastEvaluatedExpression: '',
          error: null,
          justEvaluated: false,
        };
      }
      const lastChar = base[base.length - 1];
      if (
        lastChar === ')' ||
        lastChar === 'π' ||
        lastChar === 'e' ||
        lastChar === '%' ||
        lastChar === '!'
      ) {
        return {
          ...prev,
          expression: base + '×0.',
          error: null,
          justEvaluated: false,
        };
      }
      if (isBinaryOperator(lastChar) || lastChar === '(') {
        return {
          ...prev,
          expression: base + '0.',
          error: null,
          justEvaluated: false,
        };
      }
      let idx = base.length - 1;
      while (idx >= 0 && (isDigitChar(base[idx]) || base[idx] === '.')) {
        if (base[idx] === '.') return prev;
        idx--;
      }
      return {
        ...prev,
        expression: base + '.',
        error: null,
        justEvaluated: false,
      };
    });
  }, []);

  const onOperator = useCallback((op: string) => {
    setInternal((prev) => {
      const current = prev.expression;
      if (!current) {
        if (op === '−' || op === '-') {
          return {
            ...prev,
            expression: '−',
            error: null,
            justEvaluated: false,
          };
        }
        return prev;
      }
      const last = current[current.length - 1];
      let next: string;
      if (last === '(') {
        next = op === '−' || op === '-' ? current + '−' : current;
      } else if (isBinaryOperator(last)) {
        if (current.length === 1 && (last === '−' || last === '-')) {
          next = current;
        } else {
          next = current.slice(0, -1) + op;
        }
      } else {
        next = current + op;
      }
      return {
        ...prev,
        expression: next,
        lastEvaluatedExpression: prev.justEvaluated
          ? ''
          : prev.lastEvaluatedExpression,
        error: null,
        justEvaluated: false,
      };
    });
  }, []);

  const onPercentage = useCallback(() => {
    setInternal((prev) => {
      const current = prev.expression;
      if (!current) return prev;
      const last = current[current.length - 1];
      if (
        isDigitChar(last) ||
        last === ')' ||
        last === 'π' ||
        last === 'e' ||
        last === '!'
      ) {
        return {
          ...prev,
          expression: current + '%',
          error: null,
          justEvaluated: false,
        };
      }
      return prev;
    });
  }, []);

  const onLeftParen = useCallback(() => {
    setInternal((prev) => {
      const base = prev.justEvaluated ? '' : prev.expression;
      return {
        ...prev,
        expression: base + '(',
        lastEvaluatedExpression: prev.justEvaluated
          ? ''
          : prev.lastEvaluatedExpression,
        error: null,
        justEvaluated: false,
      };
    });
  }, []);

  const onRightParen = useCallback(() => {
    setInternal((prev) => {
      const current = prev.expression;
      if (!current) return prev;
      const openCount = [...current].filter((c) => c === '(').length;
      const closeCount = [...current].filter((c) => c === ')').length;
      if (openCount <= closeCount) return prev;
      const last = current[current.length - 1];
      if (last === '(' || isBinaryOperator(last)) return prev;
      return {
        ...prev,
        expression: current + ')',
        error: null,
        justEvaluated: false,
      };
    });
  }, []);

  const onNegate = useCallback(() => {
    setInternal((prev) => {
      const current = prev.expression;
      if (!current || current === '0') {
        return { ...prev, expression: '−', error: null, justEvaluated: false };
      }
      if (current === '−' || current === '-') {
        return { ...prev, expression: '', error: null, justEvaluated: false };
      }
      if (
        (current.startsWith('−') || current.startsWith('-')) &&
        /^[0-9.E]+$/.test(current.slice(1))
      ) {
        return {
          ...prev,
          expression: current.slice(1),
          error: null,
          justEvaluated: false,
        };
      }
      if (/^[0-9.E]+$/.test(current)) {
        return {
          ...prev,
          expression: `−${current}`,
          error: null,
          justEvaluated: false,
        };
      }
      const wrappedMatch = current.match(/\(−([0-9.]+)\)$/);
      if (wrappedMatch && wrappedMatch.index !== undefined) {
        return {
          ...prev,
          expression: current.slice(0, wrappedMatch.index) + wrappedMatch[1],
          error: null,
          justEvaluated: false,
        };
      }
      if (current.endsWith('(−')) {
        return {
          ...prev,
          expression: current.slice(0, -2),
          error: null,
          justEvaluated: false,
        };
      }
      const numMatch = current.match(/([0-9.]+)$/);
      if (numMatch && numMatch.index !== undefined) {
        return {
          ...prev,
          expression:
            current.slice(0, numMatch.index) + `(−${numMatch[1]})`,
          error: null,
          justEvaluated: false,
        };
      }
      return {
        ...prev,
        expression: current + '(−',
        error: null,
        justEvaluated: false,
      };
    });
  }, []);

  const onScientificFunction = useCallback((functionPrefix: string) => {
    setInternal((prev) => {
      const base = prev.justEvaluated ? '' : prev.expression;
      return {
        ...prev,
        expression: base + functionPrefix,
        lastEvaluatedExpression: prev.justEvaluated
          ? ''
          : prev.lastEvaluatedExpression,
        error: null,
        justEvaluated: false,
      };
    });
  }, []);

  const onConstant = useCallback((constant: string) => {
    setInternal((prev) => {
      const base = prev.justEvaluated ? '' : prev.expression;
      return {
        ...prev,
        expression: base + constant,
        lastEvaluatedExpression: prev.justEvaluated
          ? ''
          : prev.lastEvaluatedExpression,
        error: null,
        justEvaluated: false,
      };
    });
  }, []);

  const onPowerExponent = useCallback((exponent: string) => {
    setInternal((prev) => {
      const current = prev.expression;
      if (!current) return prev;
      const last = current[current.length - 1];
      if (
        isDigitChar(last) ||
        last === ')' ||
        last === 'π' ||
        last === 'e' ||
        last === '%' ||
        last === '!'
      ) {
        return {
          ...prev,
          expression: `${current}^${exponent}`,
          error: null,
          justEvaluated: false,
        };
      }
      return prev;
    });
  }, []);

  const onFactorial = useCallback(() => {
    setInternal((prev) => {
      const current = prev.expression;
      if (!current) return prev;
      const last = current[current.length - 1];
      if (isDigitChar(last) || last === ')' || last === 'π' || last === 'e') {
        return {
          ...prev,
          expression: current + '!',
          error: null,
          justEvaluated: false,
        };
      }
      return prev;
    });
  }, []);

  const onDelete = useCallback(() => {
    setInternal((prev) => {
      const current = prev.expression;
      if (!current) return { ...prev, error: null };

      const multiTokens = [
        'sin⁻¹(',
        'cos⁻¹(',
        'tan⁻¹(',
        'sin(',
        'cos(',
        'tan(',
        'log(',
        'ln(',
        '√(',
      ];
      const matched = multiTokens.find((tok) => current.endsWith(tok));
      const next = matched
        ? current.slice(0, current.length - matched.length)
        : current.slice(0, -1);

      return {
        ...prev,
        expression: next,
        error: null,
        justEvaluated: false,
      };
    });
  }, []);

  const onClearAll = useCallback(() => {
    setInternal((prev) => ({
      ...INITIAL_CALC_STATE,
      isInverseTrig: prev.isInverseTrig,
    }));
  }, []);

  const onToggleInverseTrig = useCallback(() => {
    setInternal((prev) => ({ ...prev, isInverseTrig: !prev.isInverseTrig }));
  }, []);

  const onToggleAngleMode = useCallback(() => {
    setPreferences((prev) => ({
      ...prev,
      angleMode: prev.angleMode === 'DEG' ? 'RAD' : 'DEG',
    }));
  }, []);

  const onEquals = useCallback(
    (isScientific: boolean) => {
      const expr = internal.expression;
      if (!expr) return;

      const result = engineRef.current.evaluate(expr, preferences.angleMode, true);
      if (result.ok) {
        const formattedRes = NumberFormatter.formatResult(
          result.rawString,
          preferences.thousandsSeparator,
        );
        const formattedExpr = NumberFormatter.formatExpression(
          expr,
          preferences.thousandsSeparator,
        );
        setInternal((prev) => ({
          ...prev,
          expression: result.rawString,
          lastEvaluatedExpression: formattedExpr,
          error: null,
          justEvaluated: true,
        }));
        if (expr !== result.rawString) {
          const newItem: HistoryItem = {
            id: `${Date.now()}_${Math.random().toString(36).slice(2, 8)}`,
            expression: expr,
            result: formattedRes,
            rawResult: result.rawString,
            angleMode: preferences.angleMode,
            isScientific,
            timestamp: Date.now(),
          };
          setHistory((prev) => [newItem, ...prev]);
        }
      } else {
        setInternal((prev) => ({ ...prev, error: result.error }));
      }
    },
    [
      internal.expression,
      preferences.angleMode,
      preferences.thousandsSeparator,
    ],
  );

  const onMemoryClear = useCallback(() => {
    setPreferences((prev) => ({ ...prev, memoryValue: 0 }));
  }, []);

  const onMemoryRecall = useCallback(() => {
    const mem = preferences.memoryValue;
    const memStr = NumberFormatter.formatDouble(mem, false).replace(/-/g, '−');
    setInternal((prev) => {
      let next: string;
      if (!prev.expression || prev.justEvaluated) {
        next = memStr;
      } else {
        const last = prev.expression[prev.expression.length - 1];
        if (isBinaryOperator(last) || last === '(') {
          next =
            mem < 0
              ? `${prev.expression}(${memStr})`
              : `${prev.expression}${memStr}`;
        } else {
          next =
            mem < 0
              ? `${prev.expression}×(${memStr})`
              : `${prev.expression}×${memStr}`;
        }
      }
      return {
        ...prev,
        expression: next,
        error: null,
        justEvaluated: false,
      };
    });
  }, [preferences.memoryValue]);

  const modifyMemory = useCallback(
    (add: boolean) => {
      const expr = internal.expression;
      if (!expr) return;
      const result = engineRef.current.evaluate(expr, preferences.angleMode, true);
      if (result.ok) {
        setPreferences((prev) => ({
          ...prev,
          memoryValue: add
            ? prev.memoryValue + result.value
            : prev.memoryValue - result.value,
        }));
      } else {
        setInternal((prev) => ({ ...prev, error: result.error }));
      }
    },
    [internal.expression, preferences.angleMode],
  );

  const onMemoryAdd = useCallback(() => modifyMemory(true), [modifyMemory]);
  const onMemorySubtract = useCallback(() => modifyMemory(false), [modifyMemory]);

  const loadFromHistory = useCallback(
    (item: HistoryItem) => {
      setInternal((prev) => ({
        ...prev,
        expression: item.rawResult,
        lastEvaluatedExpression: NumberFormatter.formatExpression(
          item.expression,
          preferences.thousandsSeparator,
        ),
        error: null,
        justEvaluated: true,
      }));
    },
    [preferences.thousandsSeparator],
  );

  const deleteHistoryItem = useCallback((id: string) => {
    setHistory((prev) => prev.filter((item) => item.id !== id));
  }, []);

  const clearAllHistory = useCallback(() => {
    setHistory([]);
  }, []);

  const setThemeMode = useCallback((themeMode: ThemeMode) => {
    setPreferences((prev) => ({ ...prev, themeMode }));
  }, []);

  const setAngleMode = useCallback((angleMode: AngleMode) => {
    setPreferences((prev) => ({ ...prev, angleMode }));
  }, []);

  const setThousandsSeparator = useCallback((thousandsSeparator: boolean) => {
    setPreferences((prev) => ({ ...prev, thousandsSeparator }));
  }, []);

  const setHapticFeedback = useCallback((hapticFeedback: boolean) => {
    setPreferences((prev) => ({ ...prev, hapticFeedback }));
  }, []);

  const getCopyableResult = useCallback(() => {
    if (formattedPreview) return formattedPreview;
    if (state.expression) return formattedExpression;
    return '0';
  }, [formattedPreview, formattedExpression, state.expression]);

  return {
    state,
    formattedExpression,
    formattedPreview,
    formattedMemory,
    hasMemory,
    triggerHaptic,
    onDigit,
    onDecimal,
    onOperator,
    onPercentage,
    onLeftParen,
    onRightParen,
    onNegate,
    onScientificFunction,
    onConstant,
    onPowerExponent,
    onFactorial,
    onDelete,
    onClearAll,
    onToggleInverseTrig,
    onToggleAngleMode,
    onEquals,
    onMemoryClear,
    onMemoryRecall,
    onMemoryAdd,
    onMemorySubtract,
    loadFromHistory,
    deleteHistoryItem,
    clearAllHistory,
    setThemeMode,
    setAngleMode,
    setThousandsSeparator,
    setHapticFeedback,
    getCopyableResult,
  };
}
