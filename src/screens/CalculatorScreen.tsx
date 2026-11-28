import { BasicKeypad } from '../components/BasicKeypad';
import { CalculatorDisplay } from '../components/CalculatorDisplay';
import { NumberFormatter } from '../engine/NumberFormatter';
import type { CalculatorState, HistoryItem } from '../types/calculator';

interface CalculatorScreenProps {
  state: CalculatorState;
  formattedExpression: string;
  formattedPreview: string;
  formattedMemory: string;
  hasMemory: boolean;
  onHaptic: () => void;
  onDigit: (digit: string) => void;
  onDecimal: () => void;
  onOperator: (op: string) => void;
  onPercentage: () => void;
  onLeftParen: () => void;
  onRightParen: () => void;
  onNegate: () => void;
  onDelete: () => void;
  onClearAll: () => void;
  onEquals: () => void;
  onToggleAngleMode: () => void;
  onCopyResult: () => void;
  onMemoryClear: () => void;
  onMemoryRecall: () => void;
  onMemoryAdd: () => void;
  onMemorySubtract: () => void;
  onSelectHistoryItem: (item: HistoryItem) => void;
}

export function CalculatorScreen({
  state,
  formattedExpression,
  formattedPreview,
  formattedMemory,
  hasMemory,
  onHaptic,
  onDigit,
  onDecimal,
  onOperator,
  onPercentage,
  onLeftParen,
  onRightParen,
  onNegate,
  onDelete,
  onClearAll,
  onEquals,
  onToggleAngleMode,
  onCopyResult,
  onMemoryClear,
  onMemoryRecall,
  onMemoryAdd,
  onMemorySubtract,
  onSelectHistoryItem,
}: CalculatorScreenProps) {
  const recentHistory = state.history.slice(0, 20);

  return (
    <div className="mx-auto flex h-full w-full max-w-[1160px] flex-col md:grid md:grid-cols-[1.35fr_0.75fr] md:gap-4">
      {/* Main Calculator Column */}
      <div className="mx-auto flex h-full w-full max-w-[540px] flex-col gap-3 md:max-w-none">
        <CalculatorDisplay
          expression={state.expression}
          formattedExpression={formattedExpression}
          formattedPreview={formattedPreview}
          lastEvaluatedExpression={state.lastEvaluatedExpression}
          justEvaluated={state.justEvaluated}
          error={state.error}
          angleMode={state.preferences.angleMode}
          hasMemory={hasMemory}
          formattedMemory={formattedMemory}
          showAngleBadge={false}
          onToggleAngleMode={onToggleAngleMode}
          onCopyResult={onCopyResult}
        />

        <BasicKeypad
          hasMemory={hasMemory}
          onHaptic={onHaptic}
          onDigit={onDigit}
          onDecimal={onDecimal}
          onOperator={onOperator}
          onPercentage={onPercentage}
          onLeftParen={onLeftParen}
          onRightParen={onRightParen}
          onNegate={onNegate}
          onDelete={onDelete}
          onClearAll={onClearAll}
          onEquals={onEquals}
          onMemoryClear={onMemoryClear}
          onMemoryRecall={onMemoryRecall}
          onMemoryAdd={onMemoryAdd}
          onMemorySubtract={onMemorySubtract}
        />
      </div>

      {/* Supporting Recent History Pane for Wide Viewports */}
      <section
        aria-label="Recent Calculations"
        className="hidden h-full flex-col overflow-hidden rounded-[24px] border p-4 md:flex"
        style={{
          backgroundColor: 'var(--taju-surface)',
          borderColor: 'var(--taju-border)',
        }}
      >
        <h2
          className="font-display text-[16px] font-semibold"
          style={{ color: 'var(--taju-gold)' }}
        >
          Recent Calculations
        </h2>

        <div className="taju-scroll mt-2.5 flex-1 overflow-y-auto pr-1">
          {recentHistory.length === 0 ? (
            <p className="text-[13px] leading-relaxed" style={{ color: 'var(--taju-muted)' }}>
              Completed calculations appear here for quick reuse.
            </p>
          ) : (
            <div className="flex flex-col gap-2 pb-2">
              {recentHistory.map((item) => {
                const expr = NumberFormatter.formatExpression(
                  item.expression,
                  state.preferences.thousandsSeparator,
                );
                const res = NumberFormatter.formatResult(
                  item.rawResult,
                  state.preferences.thousandsSeparator,
                );
                return (
                  <button
                    key={item.id}
                    type="button"
                    onClick={() => onSelectHistoryItem(item)}
                    className="w-full rounded-[14px] border p-3 text-left transition-colors hover:brightness-105 cursor-pointer focus-visible:outline-2 focus-visible:outline-[var(--taju-gold)]"
                    style={{
                      backgroundColor: 'var(--taju-surface-elevated)',
                      borderColor: 'var(--taju-border)',
                    }}
                  >
                    <div
                      className="font-mono-num truncate text-[12px]"
                      style={{ color: 'var(--taju-muted)' }}
                    >
                      {expr}
                    </div>
                    <div
                      className="font-display tabular-nums mt-0.5 truncate text-[17px] font-semibold"
                      style={{ color: 'var(--taju-gold)' }}
                    >
                      = {res}
                    </div>
                  </button>
                );
              })}
            </div>
          )}
        </div>
      </section>
    </div>
  );
}
