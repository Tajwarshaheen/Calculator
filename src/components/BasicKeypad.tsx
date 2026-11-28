import { CalculatorButton } from './CalculatorButton';

interface BasicKeypadProps {
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
  onMemoryClear: () => void;
  onMemoryRecall: () => void;
  onMemoryAdd: () => void;
  onMemorySubtract: () => void;
}

export function BasicKeypad({
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
  onMemoryClear,
  onMemoryRecall,
  onMemoryAdd,
  onMemorySubtract,
}: BasicKeypadProps) {
  return (
    <div className="flex flex-1 flex-col gap-2.5">
      {/* Memory & Brackets Utility Strip */}
      <div className="grid h-12 grid-cols-6 gap-2">
        <CalculatorButton
          label="MC"
          onClick={onMemoryClear}
          role="MEMORY"
          testId="btn_mc"
          ariaLabel="Memory Clear"
          onHaptic={onHaptic}
          radiusClassName="rounded-[14px]"
        />
        <CalculatorButton
          label="MR"
          onClick={onMemoryRecall}
          role="MEMORY"
          testId="btn_mr"
          ariaLabel="Memory Recall"
          isActive={hasMemory}
          onHaptic={onHaptic}
          radiusClassName="rounded-[14px]"
        />
        <CalculatorButton
          label="M+"
          onClick={onMemoryAdd}
          role="MEMORY"
          testId="btn_m_plus"
          ariaLabel="Memory Add"
          onHaptic={onHaptic}
          radiusClassName="rounded-[14px]"
        />
        <CalculatorButton
          label="M-"
          onClick={onMemorySubtract}
          role="MEMORY"
          testId="btn_m_minus"
          ariaLabel="Memory Subtract"
          onHaptic={onHaptic}
          radiusClassName="rounded-[14px]"
        />
        <CalculatorButton
          label="("
          onClick={onLeftParen}
          role="SCIENTIFIC"
          testId="btn_left_paren"
          ariaLabel="Open parenthesis"
          onHaptic={onHaptic}
          sizeClassName="text-[18px] font-medium"
          radiusClassName="rounded-[14px]"
        />
        <CalculatorButton
          label=")"
          onClick={onRightParen}
          role="SCIENTIFIC"
          testId="btn_right_paren"
          ariaLabel="Close parenthesis"
          onHaptic={onHaptic}
          sizeClassName="text-[18px] font-medium"
          radiusClassName="rounded-[14px]"
        />
      </div>

      {/* 5x4 Main Grid */}
      <div className="grid flex-1 grid-cols-4 grid-rows-5 gap-2.5">
        {/* Row 1 */}
        <CalculatorButton
          label="AC"
          onClick={onClearAll}
          role="ACTION"
          testId="btn_ac"
          ariaLabel="Clear All"
          onHaptic={onHaptic}
        />
        <CalculatorButton
          label="DEL"
          onClick={onDelete}
          role="ACTION"
          testId="btn_del"
          ariaLabel="Delete last character"
          onHaptic={onHaptic}
        />
        <CalculatorButton
          label="%"
          onClick={onPercentage}
          role="ACTION"
          testId="btn_percent"
          ariaLabel="Percent"
          onHaptic={onHaptic}
          sizeClassName="text-[21px] font-semibold"
        />
        <CalculatorButton
          label="÷"
          onClick={() => onOperator('÷')}
          role="OPERATOR"
          testId="btn_divide"
          ariaLabel="Divide"
          onHaptic={onHaptic}
        />

        {/* Row 2 */}
        {['7', '8', '9'].map((d) => (
          <CalculatorButton
            key={d}
            label={d}
            onClick={() => onDigit(d)}
            role="DIGIT"
            testId={`btn_${d}`}
            onHaptic={onHaptic}
          />
        ))}
        <CalculatorButton
          label="×"
          onClick={() => onOperator('×')}
          role="OPERATOR"
          testId="btn_multiply"
          ariaLabel="Multiply"
          onHaptic={onHaptic}
        />

        {/* Row 3 */}
        {['4', '5', '6'].map((d) => (
          <CalculatorButton
            key={d}
            label={d}
            onClick={() => onDigit(d)}
            role="DIGIT"
            testId={`btn_${d}`}
            onHaptic={onHaptic}
          />
        ))}
        <CalculatorButton
          label="−"
          onClick={() => onOperator('−')}
          role="OPERATOR"
          testId="btn_subtract"
          ariaLabel="Subtract"
          onHaptic={onHaptic}
        />

        {/* Row 4 */}
        {['1', '2', '3'].map((d) => (
          <CalculatorButton
            key={d}
            label={d}
            onClick={() => onDigit(d)}
            role="DIGIT"
            testId={`btn_${d}`}
            onHaptic={onHaptic}
          />
        ))}
        <CalculatorButton
          label="+"
          onClick={() => onOperator('+')}
          role="OPERATOR"
          testId="btn_add"
          ariaLabel="Add"
          onHaptic={onHaptic}
        />

        {/* Row 5 */}
        <CalculatorButton
          label="+/−"
          onClick={onNegate}
          role="DIGIT"
          testId="btn_negate"
          ariaLabel="Toggle positive or negative"
          onHaptic={onHaptic}
          sizeClassName="text-[19px] font-medium"
        />
        <CalculatorButton
          label="0"
          onClick={() => onDigit('0')}
          role="DIGIT"
          testId="btn_0"
          onHaptic={onHaptic}
        />
        <CalculatorButton
          label="."
          onClick={onDecimal}
          role="DIGIT"
          testId="btn_decimal"
          ariaLabel="Decimal point"
          onHaptic={onHaptic}
        />
        <CalculatorButton
          label="="
          onClick={onEquals}
          role="EQUALS"
          testId="btn_equals"
          ariaLabel="Equals"
          onHaptic={onHaptic}
        />
      </div>
    </div>
  );
}
