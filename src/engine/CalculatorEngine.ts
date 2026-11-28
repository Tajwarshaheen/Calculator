import type { AngleMode, CalcError } from '../types/calculator';

export type EvalResult =
  | { ok: true; value: number; rawString: string }
  | { ok: false; error: CalcError };

type Token =
  | { type: 'number'; value: number }
  | { type: 'operator'; symbol: '+' | '-' | '*' | '/' | '^' }
  | { type: 'unary'; symbol: '+' | '-' }
  | { type: 'postfix'; symbol: '%' | '!' }
  | { type: 'function'; name: CalcFunctionName }
  | { type: 'lparen' }
  | { type: 'rparen' };

type CalcFunctionName =
  | 'sin'
  | 'cos'
  | 'tan'
  | 'asin'
  | 'acos'
  | 'atan'
  | 'sqrt'
  | 'log'
  | 'ln';

const VALID_FUNCTIONS = new Set<string>([
  'sin',
  'cos',
  'tan',
  'asin',
  'acos',
  'atan',
  'sqrt',
  'log',
  'ln',
]);

function isDigit(ch: string): boolean {
  return ch >= '0' && ch <= '9';
}

function isLetter(ch: string): boolean {
  return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
}

function toRadians(deg: number): number {
  return (deg * Math.PI) / 180;
}

function toDegrees(rad: number): number {
  return (rad * 180) / Math.PI;
}

export class CalculatorEngine {
  evaluate(
    expression: string,
    angleMode: AngleMode = 'DEG',
    autoCloseParentheses = true,
  ): EvalResult {
    const trimmed = expression.trim();
    if (!trimmed) {
      return { ok: false, error: 'INVALID_EXPRESSION' };
    }

    const normalized = this.normalizeExpression(trimmed, autoCloseParentheses);
    if (normalized === null) {
      return { ok: false, error: 'INVALID_EXPRESSION' };
    }

    const tokens = this.tokenize(normalized);
    if (tokens === null || tokens.length === 0) {
      return { ok: false, error: 'INVALID_EXPRESSION' };
    }

    const withImplicitMul = this.insertImplicitMultiplication(tokens);
    const rpn = this.toRpn(withImplicitMul);
    if (rpn === null) {
      return { ok: false, error: 'INVALID_EXPRESSION' };
    }

    return this.evaluateRpn(rpn, angleMode);
  }

  private normalizeExpression(
    input: string,
    autoCloseParentheses: boolean,
  ): string | null {
    const clean = input
      .replace(/,/g, '')
      .replace(/ /g, '')
      .replace(/×/g, '*')
      .replace(/÷/g, '/')
      .replace(/−/g, '-')
      .replace(/sin⁻¹/g, 'asin')
      .replace(/cos⁻¹/g, 'acos')
      .replace(/tan⁻¹/g, 'atan')
      .replace(/√/g, 'sqrt');

    let openCount = 0;
    let out = '';

    for (let i = 0; i < clean.length; i++) {
      const ch = clean[i];
      if (ch === '(') {
        openCount++;
      } else if (ch === ')') {
        openCount--;
        if (openCount < 0) return null;
      }
      out += ch;
    }

    if (openCount > 0) {
      if (!autoCloseParentheses) return null;
      out += ')'.repeat(openCount);
    }

    return out;
  }

  private tokenize(expr: string): Token[] | null {
    const tokens: Token[] = [];
    let i = 0;
    const n = expr.length;

    while (i < n) {
      const ch = expr[i];

      if (isDigit(ch) || ch === '.') {
        const start = i;
        let hasDot = ch === '.';
        i++;
        while (i < n && (isDigit(expr[i]) || expr[i] === '.')) {
          if (expr[i] === '.') {
            if (hasDot) return null;
            hasDot = true;
          }
          i++;
        }
        // Scientific notation using uppercase E (e.g., 1.23E-5 or 1.23E+12)
        if (i < n && expr[i] === 'E') {
          i++;
          if (i < n && (expr[i] === '+' || expr[i] === '-')) {
            i++;
          }
          const expStart = i;
          while (i < n && isDigit(expr[i])) {
            i++;
          }
          if (i === expStart) return null;
        }
        const numStr = expr.slice(start, i);
        if (numStr === '.') return null;
        const num = Number(numStr);
        if (!Number.isFinite(num)) return null;
        tokens.push({ type: 'number', value: num });
      } else if (ch === 'π') {
        tokens.push({ type: 'number', value: Math.PI });
        i++;
      } else if (ch === 'e') {
        tokens.push({ type: 'number', value: Math.E });
        i++;
      } else if (ch === '(') {
        tokens.push({ type: 'lparen' });
        i++;
      } else if (ch === ')') {
        tokens.push({ type: 'rparen' });
        i++;
      } else if (ch === '%' || ch === '!') {
        tokens.push({ type: 'postfix', symbol: ch });
        i++;
      } else if (ch === '+' || ch === '-') {
        const prev = tokens[tokens.length - 1];
        const isUnary =
          !prev ||
          prev.type === 'operator' ||
          prev.type === 'unary' ||
          prev.type === 'lparen' ||
          prev.type === 'function';
        if (isUnary) {
          tokens.push({ type: 'unary', symbol: ch });
        } else {
          tokens.push({ type: 'operator', symbol: ch });
        }
        i++;
      } else if (ch === '*' || ch === '/' || ch === '^') {
        tokens.push({ type: 'operator', symbol: ch });
        i++;
      } else if (isLetter(ch)) {
        const start = i;
        while (i < n && isLetter(expr[i])) {
          i++;
        }
        const name = expr.slice(start, i).toLowerCase();
        if (VALID_FUNCTIONS.has(name)) {
          tokens.push({ type: 'function', name: name as CalcFunctionName });
        } else if (name === 'pi') {
          tokens.push({ type: 'number', value: Math.PI });
        } else {
          return null;
        }
      } else {
        return null;
      }
    }

    return tokens;
  }

  private insertImplicitMultiplication(tokens: Token[]): Token[] {
    if (tokens.length < 2) return tokens;
    const result: Token[] = [];
    for (let idx = 0; idx < tokens.length; idx++) {
      const current = tokens[idx];
      if (idx > 0) {
        const prev = tokens[idx - 1];
        const prevEndsValue =
          prev.type === 'number' ||
          prev.type === 'rparen' ||
          prev.type === 'postfix';
        const currentStartsValue =
          current.type === 'number' ||
          current.type === 'lparen' ||
          current.type === 'function';
        if (prevEndsValue && currentStartsValue) {
          result.push({ type: 'operator', symbol: '*' });
        }
      }
      result.push(current);
    }
    return result;
  }

  private precedence(token: Token): number {
    switch (token.type) {
      case 'operator':
        if (token.symbol === '+' || token.symbol === '-') return 1;
        if (token.symbol === '*' || token.symbol === '/') return 2;
        if (token.symbol === '^') return 4;
        return 0;
      case 'unary':
        return 3;
      case 'postfix':
        return 5;
      case 'function':
        return 6;
      default:
        return 0;
    }
  }

  private isRightAssociative(token: Token): boolean {
    if (token.type === 'operator') return token.symbol === '^';
    if (token.type === 'unary') return true;
    return false;
  }

  private toRpn(tokens: Token[]): Token[] | null {
    const output: Token[] = [];
    const stack: Token[] = [];

    for (const token of tokens) {
      switch (token.type) {
        case 'number':
          output.push(token);
          break;
        case 'postfix':
          while (
            stack.length > 0 &&
            this.precedence(stack[stack.length - 1]) >= this.precedence(token)
          ) {
            output.push(stack.pop()!);
          }
          output.push(token);
          break;
        case 'function':
          stack.push(token);
          break;
        case 'operator':
        case 'unary':
          while (stack.length > 0) {
            const top = stack[stack.length - 1];
            if (top.type === 'lparen') break;
            const pTop = this.precedence(top);
            const pCurr = this.precedence(token);
            if (
              pTop > pCurr ||
              (pTop === pCurr && !this.isRightAssociative(token))
            ) {
              output.push(stack.pop()!);
            } else {
              break;
            }
          }
          stack.push(token);
          break;
        case 'lparen':
          stack.push(token);
          break;
        case 'rparen': {
          let foundLeft = false;
          while (stack.length > 0) {
            const top = stack.pop()!;
            if (top.type === 'lparen') {
              foundLeft = true;
              break;
            }
            output.push(top);
          }
          if (!foundLeft) return null;
          if (stack.length > 0 && stack[stack.length - 1].type === 'function') {
            output.push(stack.pop()!);
          }
          break;
        }
      }
    }

    while (stack.length > 0) {
      const top = stack.pop()!;
      if (top.type === 'lparen' || top.type === 'rparen') return null;
      output.push(top);
    }

    return output;
  }

  private evaluateRpn(rpn: Token[], angleMode: AngleMode): EvalResult {
    const stack: number[] = [];

    for (const token of rpn) {
      switch (token.type) {
        case 'number':
          stack.push(token.value);
          break;

        case 'unary': {
          if (stack.length === 0) {
            return { ok: false, error: 'INVALID_EXPRESSION' };
          }
          const v = stack.pop()!;
          stack.push(token.symbol === '-' ? -v : v);
          break;
        }

        case 'postfix': {
          if (stack.length === 0) {
            return { ok: false, error: 'INVALID_EXPRESSION' };
          }
          const v = stack.pop()!;
          if (token.symbol === '%') {
            stack.push(v / 100);
          } else {
            const fact = this.factorial(v);
            if (fact === null) {
              return { ok: false, error: 'MATH_DOMAIN' };
            }
            if (!Number.isFinite(fact)) {
              return { ok: false, error: 'OVERFLOW' };
            }
            stack.push(fact);
          }
          break;
        }

        case 'operator': {
          if (stack.length < 2) {
            return { ok: false, error: 'INVALID_EXPRESSION' };
          }
          const b = stack.pop()!;
          const a = stack.pop()!;
          let res: number;

          switch (token.symbol) {
            case '+':
              res = a + b;
              break;
            case '-':
              res = a - b;
              break;
            case '*':
              res = a * b;
              break;
            case '/':
              if (b === 0) {
                return { ok: false, error: 'DIVIDE_BY_ZERO' };
              }
              res = a / b;
              break;
            case '^':
              if (a === 0 && b < 0) {
                return { ok: false, error: 'DIVIDE_BY_ZERO' };
              }
              if (a < 0 && b !== Math.floor(b)) {
                return { ok: false, error: 'MATH_DOMAIN' };
              }
              res = Math.pow(a, b);
              break;
          }

          if (Number.isNaN(res)) {
            return { ok: false, error: 'MATH_DOMAIN' };
          }
          if (!Number.isFinite(res)) {
            return { ok: false, error: 'OVERFLOW' };
          }
          stack.push(res);
          break;
        }

        case 'function': {
          if (stack.length === 0) {
            return { ok: false, error: 'INVALID_EXPRESSION' };
          }
          const x = stack.pop()!;
          let res: number;

          switch (token.name) {
            case 'sqrt':
              if (x < 0) return { ok: false, error: 'MATH_DOMAIN' };
              res = Math.sqrt(x);
              break;
            case 'log':
              if (x <= 0) return { ok: false, error: 'MATH_DOMAIN' };
              res = Math.log10(x);
              break;
            case 'ln':
              if (x <= 0) return { ok: false, error: 'MATH_DOMAIN' };
              res = Math.log(x);
              break;
            case 'sin': {
              const rad = angleMode === 'DEG' ? toRadians(x) : x;
              res = Math.sin(rad);
              break;
            }
            case 'cos': {
              const rad = angleMode === 'DEG' ? toRadians(x) : x;
              res = Math.cos(rad);
              break;
            }
            case 'tan': {
              const rad = angleMode === 'DEG' ? toRadians(x) : x;
              if (Math.abs(Math.cos(rad)) < 1e-12) {
                return { ok: false, error: 'MATH_DOMAIN' };
              }
              res = Math.tan(rad);
              break;
            }
            case 'asin': {
              if (x < -1 || x > 1) return { ok: false, error: 'MATH_DOMAIN' };
              const rad = Math.asin(x);
              res = angleMode === 'DEG' ? toDegrees(rad) : rad;
              break;
            }
            case 'acos': {
              if (x < -1 || x > 1) return { ok: false, error: 'MATH_DOMAIN' };
              const rad = Math.acos(x);
              res = angleMode === 'DEG' ? toDegrees(rad) : rad;
              break;
            }
            case 'atan': {
              const rad = Math.atan(x);
              res = angleMode === 'DEG' ? toDegrees(rad) : rad;
              break;
            }
          }

          if (Number.isNaN(res)) {
            return { ok: false, error: 'MATH_DOMAIN' };
          }
          if (!Number.isFinite(res)) {
            return { ok: false, error: 'OVERFLOW' };
          }
          stack.push(res);
          break;
        }

        case 'lparen':
        case 'rparen':
          return { ok: false, error: 'INVALID_EXPRESSION' };
      }
    }

    if (stack.length !== 1) {
      return { ok: false, error: 'INVALID_EXPRESSION' };
    }

    const finalValue = this.cleanFloatingPoint(stack[0]);
    if (Number.isNaN(finalValue)) {
      return { ok: false, error: 'MATH_DOMAIN' };
    }
    if (!Number.isFinite(finalValue)) {
      return { ok: false, error: 'OVERFLOW' };
    }

    const rawString = this.toRawString(finalValue);
    return { ok: true, value: finalValue, rawString };
  }

  private factorial(x: number): number | null {
    if (x < 0 || x !== Math.floor(x) || x > 170) return null;
    const n = Math.trunc(x);
    let result = 1;
    for (let k = 2; k <= n; k++) {
      result *= k;
    }
    return result;
  }

  private cleanFloatingPoint(value: number): number {
    if (!Number.isFinite(value)) return value;
    if (Math.abs(value) < 1e-13) return 0;
    const rounded = Number(value.toPrecision(12));
    return Number.isNaN(rounded) ? value : rounded;
  }

  private toRawString(value: number): string {
    if (value === 0) return '0';
    const absVal = Math.abs(value);
    if (absVal >= 1e12 || (absVal > 0 && absVal < 1e-8)) {
      return value
        .toExponential(8)
        .toUpperCase()
        .replace(/(\.\d*?[1-9])0+E/, '$1E')
        .replace(/\.0+E/, 'E')
        .replace('E+', 'E');
    }

    // Plain decimal string without scientific notation for [1e-8, 1e12)
    const str = value.toString();
    if (!str.includes('e') && !str.includes('E')) {
      return str;
    }
    return value
      .toFixed(12)
      .replace(/(\.\d*?[1-9])0+$/, '$1')
      .replace(/\.0+$/, '');
  }
}
