import { useCallback, useEffect, useRef, useState } from 'react';
import {
  TajuBottomNavigation,
  TajuNavigationRail,
} from './components/NavigationShell';
import { useCalculator } from './hooks/useCalculator';
import { AboutScreen } from './screens/AboutScreen';
import { CalculatorScreen } from './screens/CalculatorScreen';
import { HistoryScreen } from './screens/HistoryScreen';
import { ScientificScreen } from './screens/ScientificScreen';
import { SettingsScreen } from './screens/SettingsScreen';
import { SplashScreen } from './screens/SplashScreen';
import type { AppRoute, HistoryItem } from './types/calculator';

export function App() {
  const [showSplash, setShowSplash] = useState(true);
  const [currentRoute, setCurrentRoute] = useState<AppRoute>('calculator');
  const [snackbarMessage, setSnackbarMessage] = useState<string | null>(null);
  const snackbarTimerRef = useRef<number | null>(null);

  const calc = useCalculator();

  const showSnackbar = useCallback((message: string) => {
    setSnackbarMessage(message);
    if (snackbarTimerRef.current !== null) {
      window.clearTimeout(snackbarTimerRef.current);
    }
    snackbarTimerRef.current = window.setTimeout(() => {
      setSnackbarMessage(null);
    }, 2400);
  }, []);

  useEffect(() => {
    return () => {
      if (snackbarTimerRef.current !== null) {
        window.clearTimeout(snackbarTimerRef.current);
      }
    };
  }, []);

  const copyToClipboard = useCallback(
    async (value: string) => {
      try {
        if (navigator.clipboard?.writeText) {
          await navigator.clipboard.writeText(value);
        } else {
          const textarea = document.createElement('textarea');
          textarea.value = value;
          textarea.style.position = 'fixed';
          textarea.style.opacity = '0';
          document.body.appendChild(textarea);
          textarea.select();
          document.execCommand('copy');
          document.body.removeChild(textarea);
        }
        showSnackbar('Copied result to clipboard');
      } catch {
        showSnackbar('Unable to copy to clipboard');
      }
    },
    [showSnackbar],
  );

  // Physical keyboard support matching AppNavigator.kt
  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (showSplash) return;
      const isSci = currentRoute === 'scientific';
      const isCalc = currentRoute === 'calculator' || isSci;
      if (!isCalc) return;

      // Ignore browser shortcuts (Ctrl/Cmd/Alt)
      if (event.ctrlKey || event.metaKey || event.altKey) return;

      const { key } = event;
      if (/^[0-9]$/.test(key)) {
        event.preventDefault();
        calc.onDigit(key);
      } else if (key === '.') {
        event.preventDefault();
        calc.onDecimal();
      } else if (key === '+') {
        event.preventDefault();
        calc.onOperator('+');
      } else if (key === '-') {
        event.preventDefault();
        calc.onOperator('−');
      } else if (key === '*') {
        event.preventDefault();
        calc.onOperator('×');
      } else if (key === '/') {
        event.preventDefault();
        calc.onOperator('÷');
      } else if (key === '^') {
        event.preventDefault();
        calc.onOperator('^');
      } else if (key === '%') {
        event.preventDefault();
        calc.onPercentage();
      } else if (key === '(') {
        event.preventDefault();
        calc.onLeftParen();
      } else if (key === ')') {
        event.preventDefault();
        calc.onRightParen();
      } else if (key === 'Enter' || key === '=') {
        event.preventDefault();
        calc.onEquals(isSci);
      } else if (key === 'Backspace') {
        event.preventDefault();
        calc.onDelete();
      } else if (key === 'Escape' || key === 'Delete') {
        event.preventDefault();
        calc.onClearAll();
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [showSplash, currentRoute, calc]);

  const handleSelectHistoryItem = useCallback(
    (item: HistoryItem) => {
      calc.loadFromHistory(item);
      setCurrentRoute(item.isScientific ? 'scientific' : 'calculator');
    },
    [calc],
  );

  return (
    <div className="taju-backdrop relative flex h-dvh w-full overflow-hidden">
      <div className="mx-auto flex h-full w-full max-w-[1360px] gap-2 px-3 pt-2.5 pb-[78px] md:gap-4 md:px-4 md:py-3">
        {!showSplash && (
          <TajuNavigationRail
            currentRoute={currentRoute}
            onNavigate={setCurrentRoute}
          />
        )}

        <main className="h-full min-w-0 flex-1">
          {currentRoute === 'calculator' && (
            <CalculatorScreen
              state={calc.state}
              formattedExpression={calc.formattedExpression}
              formattedPreview={calc.formattedPreview}
              formattedMemory={calc.formattedMemory}
              hasMemory={calc.hasMemory}
              onHaptic={calc.triggerHaptic}
              onDigit={calc.onDigit}
              onDecimal={calc.onDecimal}
              onOperator={calc.onOperator}
              onPercentage={calc.onPercentage}
              onLeftParen={calc.onLeftParen}
              onRightParen={calc.onRightParen}
              onNegate={calc.onNegate}
              onDelete={calc.onDelete}
              onClearAll={calc.onClearAll}
              onEquals={() => calc.onEquals(false)}
              onToggleAngleMode={calc.onToggleAngleMode}
              onCopyResult={() => copyToClipboard(calc.getCopyableResult())}
              onMemoryClear={calc.onMemoryClear}
              onMemoryRecall={calc.onMemoryRecall}
              onMemoryAdd={calc.onMemoryAdd}
              onMemorySubtract={calc.onMemorySubtract}
              onSelectHistoryItem={handleSelectHistoryItem}
            />
          )}

          {currentRoute === 'scientific' && (
            <ScientificScreen
              state={calc.state}
              formattedExpression={calc.formattedExpression}
              formattedPreview={calc.formattedPreview}
              formattedMemory={calc.formattedMemory}
              hasMemory={calc.hasMemory}
              onHaptic={calc.triggerHaptic}
              onToggleAngleMode={calc.onToggleAngleMode}
              onToggleInverseTrig={calc.onToggleInverseTrig}
              onDigit={calc.onDigit}
              onDecimal={calc.onDecimal}
              onOperator={calc.onOperator}
              onPercentage={calc.onPercentage}
              onLeftParen={calc.onLeftParen}
              onRightParen={calc.onRightParen}
              onNegate={calc.onNegate}
              onScientificFunction={calc.onScientificFunction}
              onConstant={calc.onConstant}
              onPowerExponent={calc.onPowerExponent}
              onFactorial={calc.onFactorial}
              onDelete={calc.onDelete}
              onClearAll={calc.onClearAll}
              onEquals={() => calc.onEquals(true)}
              onCopyResult={() => copyToClipboard(calc.getCopyableResult())}
              onMemoryClear={calc.onMemoryClear}
              onMemoryRecall={calc.onMemoryRecall}
              onMemoryAdd={calc.onMemoryAdd}
              onMemorySubtract={calc.onMemorySubtract}
            />
          )}

          {currentRoute === 'history' && (
            <HistoryScreen
              state={calc.state}
              onSelectHistoryItem={handleSelectHistoryItem}
              onCopyResult={copyToClipboard}
              onDeleteItem={calc.deleteHistoryItem}
              onClearAll={calc.clearAllHistory}
            />
          )}

          {currentRoute === 'settings' && (
            <SettingsScreen
              state={calc.state}
              formattedMemory={calc.formattedMemory}
              hasMemory={calc.hasMemory}
              onSetThemeMode={calc.setThemeMode}
              onSetAngleMode={calc.setAngleMode}
              onSetThousandsSeparator={calc.setThousandsSeparator}
              onSetHapticFeedback={calc.setHapticFeedback}
              onResetMemory={() => {
                calc.onMemoryClear();
                showSnackbar('Memory register cleared');
              }}
              onClearHistory={() => {
                calc.clearAllHistory();
                showSnackbar('Calculation history cleared');
              }}
              onNavigateToAbout={() => setCurrentRoute('about')}
            />
          )}

          {currentRoute === 'about' && (
            <AboutScreen onBack={() => setCurrentRoute('settings')} />
          )}
        </main>
      </div>

      {!showSplash && currentRoute !== 'about' && (
        <TajuBottomNavigation
          currentRoute={currentRoute}
          onNavigate={setCurrentRoute}
        />
      )}

      {/* Snackbar notification */}
      {snackbarMessage && (
        <div
          role="status"
          aria-live="polite"
          className="font-display fixed bottom-20 left-1/2 z-40 -translate-x-1/2 rounded-2xl border px-4 py-2.5 text-[13px] font-semibold shadow-xl backdrop-blur-md md:bottom-6"
          style={{
            backgroundColor: 'var(--taju-surface-elevated)',
            borderColor: 'var(--taju-gold-border)',
            color: 'var(--taju-gold-bright)',
          }}
        >
          {snackbarMessage}
        </div>
      )}

      {showSplash && <SplashScreen onFinished={() => setShowSplash(false)} />}
    </div>
  );
}

export default App;
