import type { AngleMode } from '../types/calculator';
import { CalculatorButton } from './CalculatorButton';

interface ScientificKeypadProps {
  hasMemory: boolean;
  angleMode: AngleMode;
  isInverseTrig: boolean;
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
  onMemoryClear: () => void;
  onMemoryRecall: () => void;
  onMemoryAdd: () => void;
  onMemorySubtract: () => void;
}

export function ScientificKeypad({
  hasMemory,
  angleMode,
  isInverseTrig,
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
  onMemoryClear,
  onMemoryRecall,
  onMemoryAdd,
  onMemorySubtract,
}: ScientificKeypadProps) {
  const sinLabel = isInverseTrig ? 'sin⁻¹' : 'sin';
  const cosLabel = isInverseTrig ? 'cos⁻¹' : 'cos';
  const tanLabel = isInverseTrig ? 'tan⁻¹' : 'tan';

  return (
    <div className="flex flex-1 flex-col gap-[7px]">
      {/* Mode & Memory Bar */}
      <div className="grid h-12 grid-cols-6 gap-1.5">
        <CalculatorButton
          label={angleMode}
          onClick={onToggleAngleMode}
          role="SCIENTIFIC"
          testId="sci_btn_angle"
          ariaLabel="Toggle Degrees or Radians"
          isActive
          onHaptic={onHaptic}
          sizeClassName="text-[12px] font-semibold"
          radiusClassName="rounded-[13px]"
        />
        <CalculatorButton
          label="INV"
          onClick={onToggleInverseTrig}
          role="SCIENTIFIC"
          testId="sci_btn_inv"
          ariaLabel="Toggle inverse trigonometric functions"
          isActive={isInverseTrig}
          onHaptic={onHaptic}
          sizeClassName="text-[12px] font-semibold"
          radiusClassName="rounded-[13px]"
        />
        <CalculatorButton
          label="MC"
          onClick={onMemoryClear}
          role="MEMORY"
          testId="sci_btn_mc"
          ariaLabel="Memory Clear"
          onHaptic={onHaptic}
          radiusClassName="rounded-[13px]"
        />
        <CalculatorButton
          label="MR"
          onClick={onMemoryRecall}
          role="MEMORY"
          testId="sci_btn_mr"
          ariaLabel="Memory Recall"
          isActive={hasMemory}
          onHaptic={onHaptic}
          radiusClassName="rounded-[13px]"
        />
        <CalculatorButton
          label="M+"
          onClick={onMemoryAdd}
          role="MEMORY"
          testId="sci_btn_m_plus"
          ariaLabel="Memory Add"
          onHaptic={onHaptic}
          radiusClassName="rounded-[13px]"
        />
        <CalculatorButton
          label="M-"
          onClick={onMemorySubtract}
          role="MEMORY"
          testId="sci_btn_m_minus"
          ariaLabel="Memory Subtract"
          onHaptic={onHaptic}
          radiusClassName="rounded-[13px]"
        />
      </div>

      {/* Scientific Rows 1-3 (5 columns each) */}
      <div className="grid grid-cols-5 gap-[7px]">
        <CalculatorButton
          label="x²"
          onClick={() => onPowerExponent('2')}
          role="SCIENTIFIC"
          testId="sci_btn_square"
          ariaLabel="Square"
          onHaptic={onHaptic}
          radiusClassName="rounded-[15px]"
        />
        <CalculatorButton
          label="x³"
          onClick={() => onPowerExponent('3')}
          role="SCIENTIFIC"
          testId="sci_btn_cube"
          ariaLabel="Cube"
          onHaptic={onHaptic}
          radiusClassName="rounded-[15px]"
        />
        <CalculatorButton
          label="√"
          onClick={() => onScientificFunction('√(')}
          role="SCIENTIFIC"
          testId="sci_btn_sqrt"
          ariaLabel="Square root"
          onHaptic={onHaptic}
          sizeClassName="text-[18px] font-medium"
          radiusClassName="rounded-[15px]"
        />
        <CalculatorButton
          label="xʸ"
          onClick={() => onOperator('^')}
          role="SCIENTIFIC"
          testId="sci_btn_power"
          ariaLabel="Power"
          onHaptic={onHaptic}
          radiusClassName="rounded-[15px]"
        />
        <CalculatorButton
          label="x!"
          onClick={onFactorial}
          role="SCIENTIFIC"
          testId="sci_btn_factorial"
          ariaLabel="Factorial"
          onHaptic={onHaptic}
          radiusClassName="rounded-[15px]"
        />

        <CalculatorButton
          label={sinLabel}
          onClick={() => onScientificFunction(`${sinLabel}(`)}
          role="SCIENTIFIC"
          testId="sci_btn_sin"
          ariaLabel={sinLabel}
          isActive={isInverseTrig}
          onHaptic={onHaptic}
          sizeClassName={
            isInverseTrig
              ? 'text-[13px] font-medium'
              : 'text-[15px] font-medium'
          }
          radiusClassName="rounded-[15px]"
        />
        <CalculatorButton
          label={cosLabel}
          onClick={() => onScientificFunction(`${cosLabel}(`)}
          role="SCIENTIFIC"
          testId="sci_btn_cos"
          ariaLabel={cosLabel}
          isActive={isInverseTrig}
          onHaptic={onHaptic}
          sizeClassName={
            isInverseTrig
              ? 'text-[13px] font-medium'
              : 'text-[15px] font-medium'
          }
          radiusClassName="rounded-[15px]"
        />
        <CalculatorButton
          label={tanLabel}
          onClick={() => onScientificFunction(`${tanLabel}(`)}
          role="SCIENTIFIC"
          testId="sci_btn_tan"
          ariaLabel={tanLabel}
          isActive={isInverseTrig}
          onHaptic={onHaptic}
          sizeClassName={
            isInverseTrig
              ? 'text-[13px] font-medium'
              : 'text-[15px] font-medium'
          }
          radiusClassName="rounded-[15px]"
        />
        <CalculatorButton
          label="log"
          onClick={() => onScientificFunction('log(')}
          role="SCIENTIFIC"
          testId="sci_btn_log"
          ariaLabel="Base 10 logarithm"
          onHaptic={onHaptic}
          radiusClassName="rounded-[15px]"
        />
        <CalculatorButton
          label="ln"
          onClick={() => onScientificFunction('ln(')}
          role="SCIENTIFIC"
          testId="sci_btn_ln"
          ariaLabel="Natural logarithm"
          onHaptic={onHaptic}
          radiusClassName="rounded-[15px]"
        />

        <CalculatorButton
          label="("
          onClick={onLeftParen}
          role="SCIENTIFIC"
          testId="sci_btn_left_paren"
          ariaLabel="Open parenthesis"
          onHaptic={onHaptic}
          sizeClassName="text-[17px] font-medium"
          radiusClassName="rounded-[15px]"
        />
        <CalculatorButton
          label=")"
          onClick={onRightParen}
          role="SCIENTIFIC"
          testId="sci_btn_right_paren"
          ariaLabel="Close parenthesis"
          onHaptic={onHaptic}
          sizeClassName="text-[17px] font-medium"
          radiusClassName="rounded-[15px]"
        />
        <CalculatorButton
          label="π"
          onClick={() => onConstant('π')}
          role="SCIENTIFIC"
          testId="sci_btn_pi"
          ariaLabel="Pi"
          onHaptic={onHaptic}
          sizeClassName="text-[17px] font-medium"
          radiusClassName="rounded-[15px]"
        />
        <CalculatorButton
          label="e"
          onClick={() => onConstant('e')}
          role="SCIENTIFIC"
          testId="sci_btn_e"
          ariaLabel="Euler's number"
          onHaptic={onHaptic}
          sizeClassName="text-[17px] font-medium"
          radiusClassName="rounded-[15px]"
        />
        <CalculatorButton
          label="%"
          onClick={onPercentage}
          role="SCIENTIFIC"
          testId="sci_btn_percent"
          ariaLabel="Percent"
          onHaptic={onHaptic}
          sizeClassName="text-[17px] font-medium"
          radiusClassName="rounded-[15px]"
        />
      </div>

      {/* Basic Digits & Operators Grid (4 columns x 5 rows) */}
      <div className="grid flex-1 grid-cols-4 grid-rows-5 gap-2">
        <CalculatorButton
          label="AC"
          onClick={onClearAll}
          role="ACTION"
          testId="sci_btn_ac"
          ariaLabel="Clear All"
          onHaptic={onHaptic}
          sizeClassName="text-[17px] font-semibold"
          radiusClassName="rounded-[17px]"
        />
        <CalculatorButton
          label="DEL"
          onClick={onDelete}
          role="ACTION"
          testId="sci_btn_del"
          ariaLabel="Delete"
          onHaptic={onHaptic}
          sizeClassName="text-[17px] font-semibold"
          radiusClassName="rounded-[17px]"
        />
        <CalculatorButton
          label="+/−"
          onClick={onNegate}
          role="ACTION"
          testId="sci_btn_negate"
          ariaLabel="Negate"
          onHaptic={onHaptic}
          sizeClassName="text-[17px] font-semibold"
          radiusClassName="rounded-[17px]"
        />
        <CalculatorButton
          label="÷"
          onClick={() => onOperator('÷')}
          role="OPERATOR"
          testId="sci_btn_divide"
          ariaLabel="Divide"
          onHaptic={onHaptic}
          sizeClassName="text-[23px] font-semibold"
          radiusClassName="rounded-[17px]"
        />

        {['7', '8', '9'].map((d) => (
          <CalculatorButton
            key={d}
            label={d}
            onClick={() => onDigit(d)}
            role="DIGIT"
            testId={`sci_btn_${d}`}
            onHaptic={onHaptic}
            sizeClassName="text-[22px] font-medium"
            radiusClassName="rounded-[17px]"
          />
        ))}
        <CalculatorButton
          label="×"
          onClick={() => onOperator('×')}
          role="OPERATOR"
          testId="sci_btn_multiply"
          ariaLabel="Multiply"
          onHaptic={onHaptic}
          sizeClassName="text-[23px] font-semibold"
          radiusClassName="rounded-[17px]"
        />

        {['4', '5', '6'].map((d) => (
          <CalculatorButton
            key={d}
            label={d}
            onClick={() => onDigit(d)}
            role="DIGIT"
            testId={`sci_btn_${d}`}
            onHaptic={onHaptic}
            sizeClassName="text-[22px] font-medium"
            radiusClassName="rounded-[17px]"
          />
        ))}
        <CalculatorButton
          label="−"
          onClick={() => onOperator('−')}
          role="OPERATOR"
          testId="sci_btn_subtract"
          ariaLabel="Subtract"
          onHaptic={onHaptic}
          sizeClassName="text-[23px] font-semibold"
          radiusClassName="rounded-[17px]"
        />

        {['1', '2', '3'].map((d) => (
          <CalculatorButton
            key={d}
            label={d}
            onClick={() => onDigit(d)}
            role="DIGIT"
            testId={`sci_btn_${d}`}
            onHaptic={onHaptic}
            sizeClassName="text-[22px] font-medium"
            radiusClassName="rounded-[17px]"
          />
        ))}
        <CalculatorButton
          label="+"
          onClick={() => onOperator('+')}
          role="OPERATOR"
          testId="sci_btn_add"
          ariaLabel="Add"
          onHaptic={onHaptic}
          sizeClassName="text-[23px] font-semibold"
          radiusClassName="rounded-[17px]"
        />

        <CalculatorButton
          label="0"
          onClick={() => onDigit('0')}
          role="DIGIT"
          testId="sci_btn_0"
          onHaptic={onHaptic}
          sizeClassName="text-[22px] font-medium"
          radiusClassName="rounded-[17px]"
          className="col-span-2"
        />
        <CalculatorButton
          label="."
          onClick={onDecimal}
          role="DIGIT"
          testId="sci_btn_decimal"
          ariaLabel="Decimal point"
          onHaptic={onHaptic}
          sizeClassName="text-[22px] font-medium"
          radiusClassName="rounded-[17px]"
        />
        <CalculatorButton
          label="="
          onClick={onEquals}
          role="EQUALS"
          testId="sci_btn_equals"
          ariaLabel="Equals"
          onHaptic={onHaptic}
          sizeClassName="text-[25px] font-bold"
          radiusClassName="rounded-[17px]"
        />
      </div>
    </div>
  );
}
