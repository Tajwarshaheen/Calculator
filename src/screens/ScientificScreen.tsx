import { CalculatorDisplay } from '../components/CalculatorDisplay';
import { ScientificKeypad } from '../components/ScientificKeypad';
import type { CalculatorState } from '../types/calculator';

interface ScientificScreenProps {
  state: CalculatorState;
  formattedExpression: string;
  formattedPreview: string;
  formattedMemory: string;
  hasMemory: boolean;
  onHaptic: () => void;
  onToggleAngleMode: () => void;
  onToggleInverseTrig: () => void;
  onDigit: (digit: string) => void;
  onDecimal: () => void;
  onOperator: (op: string) => void;
  onPercentage: () => void;
  onLeftParen: () => void;
  onRightParen: () => void;
  onNegate: () => void;
  onScientificFunction: (fnPrefix: string) => void;
  onConstant: (constant: string) => void;
  onPowerExponent: (exponent: string) => void;
  onFactorial: () => void;
  onDelete: () => void;
  onClearAll: () => void;
  onEquals: () => void;
  onCopyResult: () => void;
  onMemoryClear: () => void;
  onMemoryRecall: () => void;
  onMemoryAdd: () => void;
  onMemorySubtract: () => void;
}

export function ScientificScreen({
  state,
  formattedExpression,
  formattedPreview,
  formattedMemory,
  hasMemory,
  onHaptic,
  onToggleAngleMode,
  onToggleInverseTrig,
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
  onEquals,
  onCopyResult,
  onMemoryClear,
  onMemoryRecall,
  onMemoryAdd,
  onMemorySubtract,
}: ScientificScreenProps) {
  return (
    <div className="mx-auto flex h-full w-full max-w-[600px] flex-col gap-2.5">
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
        showAngleBadge
        onToggleAngleMode={onToggleAngleMode}
        onCopyResult={onCopyResult}
      />

      <ScientificKeypad
        hasMemory={hasMemory}
        angleMode={state.preferences.angleMode}
        isInverseTrig={state.isInverseTrig}
        onHaptic={onHaptic}
        onToggleAngleMode={onToggleAngleMode}
        onToggleInverseTrig={onToggleInverseTrig}
        onDigit={onDigit}
        onDecimal={onDecimal}
        onOperator={onOperator}
        onPercentage={onPercentage}
        onLeftParen={onLeftParen}
        onRightParen={onRightParen}
        onNegate={onNegate}
        onScientificFunction={onScientificFunction}
        onConstant={onConstant}
        onPowerExponent={onPowerExponent}
        onFactorial={onFactorial}
        onDelete={onDelete}
        onClearAll={onClearAll}
        onEquals={onEquals}
        onMemoryClear={onMemoryClear}
        onMemoryRecall={onMemoryRecall}
        onMemoryAdd={onMemoryAdd}
        onMemorySubtract={onMemorySubtract}
      />
    </div>
  );
}
