import { useEffect, useRef } from 'react';
import { AlertCircle, Copy } from 'lucide-react';
import type { AngleMode, CalcError } from '../types/calculator';

interface CalculatorDisplayProps {
  expression: string;
  formattedExpression: string;
  formattedPreview: string;
  lastEvaluatedExpression: string;
  justEvaluated: boolean;
  error: CalcError | null;
  angleMode: AngleMode;
  hasMemory: boolean;
  formattedMemory: string;
  showAngleBadge: boolean;
  onToggleAngleMode: () => void;
  onCopyResult: () => void;
}

const ERROR_MESSAGES: Record<CalcError, string> = {
  DIVIDE_BY_ZERO: 'Cannot divide by zero',
  INVALID_EXPRESSION: 'Invalid expression',
  MATH_DOMAIN: 'Math domain error',
  OVERFLOW: 'Value out of range',
};

export function CalculatorDisplay({
  expression,
  formattedExpression,
  formattedPreview,
  lastEvaluatedExpression,
  justEvaluated,
  error,
  angleMode,
  hasMemory,
  formattedMemory,
  showAngleBadge,
  onToggleAngleMode,
  onCopyResult,
}: CalculatorDisplayProps) {
  const scrollRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTo({
        left: scrollRef.current.scrollWidth,
        behavior: 'smooth',
      });
    }
  }, [expression]);

  const len = formattedExpression.length;
  const exprFontClass =
    len <= 10
      ? 'text-[40px] sm:text-[44px]'
      : len <= 15
        ? 'text-[30px] sm:text-[34px]'
        : len <= 22
          ? 'text-[24px] sm:text-[26px]'
          : 'text-[20px] sm:text-[21px]';

  const errorText = error ? ERROR_MESSAGES[error] : null;

  return (
    <div
      className="w-full rounded-[26px] border px-[18px] py-[14px] shadow-md backdrop-blur-md"
      style={{
        background:
          'linear-gradient(180deg, var(--taju-surface-elevated) 0%, var(--taju-surface) 100%)',
        borderColor: 'var(--taju-border)',
      }}
    >
      {/* Top Status Row: Angle Mode Toggle / Brand Marker, Memory Indicator, Copy Button */}
      <div className="flex items-center justify-between gap-2">
        <div className="flex items-center gap-2">
          {showAngleBadge ? (
            <button
              type="button"
              data-testid="angle_mode_toggle"
              onClick={onToggleAngleMode}
              className="font-display min-h-[36px] rounded-[10px] border px-3 py-1.5 text-[12px] font-bold transition-colors whitespace-nowrap shrink-0 cursor-pointer focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-[var(--taju-gold)]"
              style={{
                backgroundColor: 'rgba(193, 154, 80, 0.16)',
                borderColor: 'var(--taju-gold-border)',
                color: 'var(--taju-gold)',
              }}
            >
              {angleMode}
            </button>
          ) : (
            <span
              className="font-display rounded-[10px] border px-2.5 py-1 text-[11px] font-bold tracking-[1.4px] whitespace-nowrap shrink-0"
              style={{
                backgroundColor: 'rgba(193, 154, 80, 0.12)',
                borderColor: 'var(--taju-gold-border)',
                color: 'var(--taju-gold)',
              }}
            >
              TAJU
            </span>
          )}

          {hasMemory && (
            <span
              className="font-mono-num max-w-[160px] truncate rounded-[10px] border px-2.5 py-1 text-[11px] font-medium text-white"
              style={{
                backgroundColor: 'rgba(1, 49, 70, 0.65)',
                borderColor: 'var(--taju-key-operator-border)',
              }}
            >
              M {formattedMemory}
            </span>
          )}
        </div>

        <button
          type="button"
          data-testid="copy_result_button"
          onClick={onCopyResult}
          aria-label="Copy result to clipboard"
          title="Copy result to clipboard"
          className="flex h-11 w-11 items-center justify-center rounded-xl transition-colors hover:bg-[var(--taju-gold)]/12 active:scale-95 cursor-pointer focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-[var(--taju-gold)]"
          style={{ color: 'var(--taju-gold)' }}
        >
          <Copy className="h-[19px] w-[19px]" />
        </button>
      </div>

      {/* Previous evaluated expression */}
      <div className="mt-1.5 flex h-[22px] w-full items-center justify-end">
        {justEvaluated && lastEvaluatedExpression && (
          <span
            className="font-mono-num truncate text-[14px]"
            style={{ color: 'var(--taju-muted)' }}
          >
            {lastEvaluatedExpression} =
          </span>
        )}
      </div>

      {/* Main Expression / Result Display */}
      <div
        ref={scrollRef}
        className="taju-scroll flex w-full justify-end overflow-x-auto py-0.5"
      >
        <span
          data-testid="calculator_display_text"
          className={`font-display tabular-nums whitespace-nowrap font-semibold leading-[1.15] ${exprFontClass}`}
          style={{
            color: justEvaluated ? 'var(--taju-gold-bright)' : 'var(--taju-text)',
          }}
        >
          {formattedExpression}
        </span>
      </div>

      {/* Live Preview or Error Message */}
      <div className="mt-1.5 flex h-[30px] w-full items-center justify-end">
        {errorText ? (
          <div
            className="flex items-center gap-1.5 rounded-[10px] px-2.5 py-1"
            style={{
              backgroundColor: 'var(--taju-error-surface)',
              color: 'var(--taju-error-text)',
            }}
          >
            <AlertCircle className="h-4 w-4 shrink-0" />
            <span
              data-testid="calculator_error_text"
              className="truncate text-[13px] font-semibold"
            >
              {errorText}
            </span>
          </div>
        ) : formattedPreview ? (
          <span
            data-testid="calculator_preview_text"
            className="font-mono-num truncate text-[20px] font-medium"
            style={{ color: 'var(--taju-gold)' }}
          >
            = {formattedPreview}
          </span>
        ) : null}
      </div>
    </div>
  );
}
