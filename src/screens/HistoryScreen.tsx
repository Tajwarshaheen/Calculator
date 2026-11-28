import { useState } from 'react';
import { Copy, History, Trash2 } from 'lucide-react';
import { NumberFormatter } from '../engine/NumberFormatter';
import type { CalculatorState, HistoryItem } from '../types/calculator';

interface HistoryScreenProps {
  state: CalculatorState;
  onSelectHistoryItem: (item: HistoryItem) => void;
  onCopyResult: (value: string) => void;
  onDeleteItem: (id: string) => void;
  onClearAll: () => void;
}

function formatTimestamp(ts: number): string {
  try {
    return new Intl.DateTimeFormat(undefined, {
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: '2-digit',
    }).format(new Date(ts));
  } catch {
    return '';
  }
}

export function HistoryScreen({
  state,
  onSelectHistoryItem,
  onCopyResult,
  onDeleteItem,
  onClearAll,
}: HistoryScreenProps) {
  const [showClearDialog, setShowClearDialog] = useState(false);

  return (
    <div className="mx-auto flex h-full w-full max-w-[680px] flex-col">
      {/* Header Row */}
      <div className="flex items-center justify-between gap-4 py-1.5">
        <div>
          <h1 className="font-display text-[24px] font-bold tracking-[0.2px]">
            Calculation History
          </h1>
          <p className="text-[13px]" style={{ color: 'var(--taju-muted)' }}>
            {state.history.length === 0
              ? 'Stored offline on device'
              : `${state.history.length} saved calculation${state.history.length === 1 ? '' : 's'}`}
          </p>
        </div>

        {state.history.length > 0 && (
          <button
            type="button"
            data-testid="clear_all_history_button"
            onClick={() => setShowClearDialog(true)}
            className="font-display flex min-h-[40px] items-center gap-1.5 rounded-[14px] border px-3 py-2 text-[13px] font-semibold transition-colors hover:brightness-110 whitespace-nowrap shrink-0 cursor-pointer focus-visible:outline-2 focus-visible:outline-[var(--taju-error-text)]"
            style={{
              backgroundColor: 'var(--taju-error-surface)',
              borderColor: 'rgba(255, 107, 107, 0.35)',
              color: 'var(--taju-error-text)',
            }}
          >
            <Trash2 className="h-[16px] w-[16px]" />
            <span>Clear All</span>
          </button>
        )}
      </div>

      {/* Content */}
      {state.history.length === 0 ? (
        <div className="flex flex-1 flex-col items-center justify-center px-6 text-center">
          <div
            className="flex h-[76px] w-[76px] items-center justify-center rounded-full border"
            style={{
              backgroundColor: 'rgba(193, 154, 80, 0.14)',
              borderColor: 'var(--taju-gold-border)',
              color: 'var(--taju-gold)',
            }}
          >
            <History className="h-9 w-9" />
          </div>
          <h2 className="font-display mt-[18px] text-[20px] font-semibold">
            No Calculations Yet
          </h2>
          <p
            className="mt-2 max-w-[420px] text-[15px] leading-relaxed"
            style={{ color: 'var(--taju-muted)' }}
          >
            Your completed calculations will appear here automatically for quick
            recall and reuse.
          </p>
        </div>
      ) : (
        <div className="taju-scroll mt-2.5 flex flex-1 flex-col gap-2.5 overflow-y-auto pb-4 pr-0.5">
          {state.history.map((item) => {
            const formattedExpr = NumberFormatter.formatExpression(
              item.expression,
              state.preferences.thousandsSeparator,
            );
            const formattedRes = NumberFormatter.formatResult(
              item.rawResult,
              state.preferences.thousandsSeparator,
            );
            const timeString = formatTimestamp(item.timestamp);

            return (
              <div
                key={item.id}
                data-testid={`history_card_${item.id}`}
                onClick={() => onSelectHistoryItem(item)}
                role="button"
                tabIndex={0}
                onKeyDown={(e) => {
                  if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    onSelectHistoryItem(item);
                  }
                }}
                className="w-full rounded-[20px] border px-4 py-3.5 text-left transition-colors hover:brightness-105 cursor-pointer focus-visible:outline-2 focus-visible:outline-[var(--taju-gold)]"
                style={{
                  backgroundColor: 'var(--taju-surface)',
                  borderColor: 'var(--taju-border)',
                }}
              >
                <div className="flex items-center justify-between gap-2">
                  <div className="flex items-center gap-2 text-[11px]">
                    <span
                      className="font-display font-bold tracking-wide"
                      style={{
                        color: item.isScientific
                          ? 'var(--taju-gold)'
                          : 'var(--taju-muted)',
                      }}
                    >
                      {item.isScientific ? `SCI · ${item.angleMode}` : 'BASIC'}
                    </span>
                    <span aria-hidden="true" style={{ color: 'var(--taju-muted)' }}>
                      ·
                    </span>
                    <span style={{ color: 'var(--taju-muted)' }}>{timeString}</span>
                  </div>

                  <div className="flex items-center">
                    <button
                      type="button"
                      aria-label="Copy result"
                      onClick={(e) => {
                        e.stopPropagation();
                        onCopyResult(formattedRes);
                      }}
                      className="flex h-10 w-10 items-center justify-center rounded-xl transition-colors hover:bg-[var(--taju-gold)]/12 cursor-pointer"
                      style={{ color: 'var(--taju-gold)' }}
                    >
                      <Copy className="h-[17px] w-[17px]" />
                    </button>
                    <button
                      type="button"
                      aria-label="Delete history item"
                      onClick={(e) => {
                        e.stopPropagation();
                        onDeleteItem(item.id);
                      }}
                      className="flex h-10 w-10 items-center justify-center rounded-xl transition-colors hover:bg-[var(--taju-error-surface)] cursor-pointer"
                      style={{ color: 'var(--taju-muted)' }}
                    >
                      <Trash2 className="h-[17px] w-[17px]" />
                    </button>
                  </div>
                </div>

                <div
                  className="font-mono-num mt-1 line-clamp-2 text-[15px]"
                  style={{ color: 'var(--taju-muted)' }}
                >
                  {formattedExpr}
                </div>
                <div
                  className="font-display tabular-nums mt-1 truncate text-[24px] font-bold"
                  style={{ color: 'var(--taju-gold-bright)' }}
                >
                  = {formattedRes}
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* Clear Confirmation Modal */}
      {showClearDialog && (
        <div
          className="fixed inset-0 z-40 flex items-center justify-center bg-black/60 p-4 backdrop-blur-sm"
          role="dialog"
          aria-modal="true"
          aria-labelledby="clear-history-title"
        >
          <div
            className="w-full max-w-[400px] rounded-[24px] border p-6 shadow-2xl"
            style={{
              backgroundColor: 'var(--taju-surface-elevated)',
              borderColor: 'var(--taju-border)',
            }}
          >
            <h2
              id="clear-history-title"
              className="font-display text-[20px] font-semibold"
            >
              Clear Calculation History?
            </h2>
            <p
              className="mt-2 text-[15px] leading-relaxed"
              style={{ color: 'var(--taju-muted)' }}
            >
              This will permanently remove all saved calculations from local
              device storage.
            </p>
            <div className="mt-6 flex items-center justify-end gap-3">
              <button
                type="button"
                onClick={() => setShowClearDialog(false)}
                className="font-display min-h-[40px] rounded-xl px-4 py-2 text-[14px] font-semibold transition-colors hover:bg-white/5 cursor-pointer"
                style={{ color: 'var(--taju-gold)' }}
              >
                Cancel
              </button>
              <button
                type="button"
                data-testid="confirm_clear_history"
                onClick={() => {
                  setShowClearDialog(false);
                  onClearAll();
                }}
                className="font-display min-h-[40px] rounded-xl px-4 py-2 text-[14px] font-bold transition-colors hover:brightness-110 cursor-pointer"
                style={{
                  backgroundColor: 'var(--taju-error-surface)',
                  color: 'var(--taju-error-text)',
                }}
              >
                Clear All
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
