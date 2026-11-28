import type { ReactNode } from 'react';

export type CalcButtonRole =
  | 'DIGIT'
  | 'OPERATOR'
  | 'SCIENTIFIC'
  | 'ACTION'
  | 'EQUALS'
  | 'MEMORY';

interface CalculatorButtonProps {
  label: ReactNode;
  onClick: () => void;
  role?: CalcButtonRole;
  ariaLabel?: string;
  isActive?: boolean;
  onHaptic?: () => void;
  className?: string;
  sizeClassName?: string;
  radiusClassName?: string;
  testId?: string;
}

export function CalculatorButton({
  label,
  onClick,
  role = 'DIGIT',
  ariaLabel,
  isActive = false,
  onHaptic,
  className = '',
  sizeClassName,
  radiusClassName = 'rounded-[20px]',
  testId,
}: CalculatorButtonProps) {
  const defaultSize = (() => {
    switch (role) {
      case 'DIGIT':
        return 'text-[24px] font-medium';
      case 'OPERATOR':
        return 'text-[25px] font-semibold';
      case 'EQUALS':
        return 'text-[28px] font-bold';
      case 'ACTION':
        return 'text-[18px] font-semibold';
      case 'SCIENTIFIC':
        return 'text-[15px] font-medium';
      case 'MEMORY':
        return 'text-[13px] font-medium';
    }
  })();

  const roleStyle: React.CSSProperties = (() => {
    switch (role) {
      case 'EQUALS':
        return {
          background:
            'linear-gradient(135deg, #DFB96E 0%, #C19A50 50%, #9A7836 100%)',
          borderColor: 'rgba(223, 185, 110, 0.75)',
          color: '#07111F',
          boxShadow: '0 6px 18px rgba(193, 154, 80, 0.25)',
        };
      case 'OPERATOR':
        return {
          background:
            'linear-gradient(180deg, var(--taju-key-operator-top) 0%, var(--taju-key-operator-bottom) 100%)',
          borderColor: 'var(--taju-key-operator-border)',
          color: '#FFFFFF',
          boxShadow: '0 2px 6px rgba(0, 0, 0, 0.14)',
        };
      case 'ACTION':
        return {
          background:
            'linear-gradient(180deg, var(--taju-key-action-top) 0%, var(--taju-key-action-bottom) 100%)',
          borderColor: 'var(--taju-gold-border)',
          color: 'var(--taju-key-action-text)',
          boxShadow: '0 2px 6px rgba(0, 0, 0, 0.08)',
        };
      case 'SCIENTIFIC':
        return isActive
          ? {
              background:
                'linear-gradient(180deg, rgba(193, 154, 80, 0.30) 0%, rgba(193, 154, 80, 0.16) 100%)',
              borderColor: 'var(--taju-gold)',
              color: 'var(--taju-gold-bright)',
            }
          : {
              background:
                'linear-gradient(180deg, var(--taju-key-function-top) 0%, var(--taju-key-function-bottom) 100%)',
              borderColor: 'var(--taju-key-digit-border)',
              color: 'var(--taju-text)',
            };
      case 'MEMORY':
        return isActive
          ? {
              background:
                'linear-gradient(180deg, rgba(193, 154, 80, 0.25) 0%, rgba(193, 154, 80, 0.12) 100%)',
              borderColor: 'var(--taju-gold)',
              color: 'var(--taju-gold)',
            }
          : {
              background:
                'linear-gradient(180deg, var(--taju-surface-elevated) 0%, var(--taju-surface) 100%)',
              borderColor: 'var(--taju-key-digit-border)',
              color: 'var(--taju-muted)',
            };
      case 'DIGIT':
      default:
        return {
          background:
            'linear-gradient(180deg, var(--taju-key-digit-top) 0%, var(--taju-key-digit-bottom) 100%)',
          borderColor: 'var(--taju-key-digit-border)',
          color: 'var(--taju-text)',
          boxShadow: '0 2px 6px rgba(0, 0, 0, 0.08)',
        };
    }
  })();

  const handleClick = () => {
    onHaptic?.();
    onClick();
  };

  return (
    <button
      type="button"
      data-testid={testId}
      aria-label={ariaLabel ?? (typeof label === 'string' ? label : undefined)}
      onClick={handleClick}
      style={roleStyle}
      className={`font-display relative flex min-h-[48px] min-w-[48px] select-none items-center justify-center border ${radiusClassName} transition-[transform,filter,opacity] duration-150 ease-[cubic-bezier(0.16,1,0.3,1)] hover:brightness-105 active:scale-[0.93] focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-[var(--taju-gold)] whitespace-nowrap shrink-0 cursor-pointer ${sizeClassName ?? defaultSize} ${className}`}
    >
      <span className="truncate px-1">{label}</span>
    </button>
  );
}
