function isDigitChar(ch: string): boolean {
  return ch >= '0' && ch <= '9';
}

function groupDigits(digits: string): string {
  if (digits.length <= 3) return digits;
  let result = '';
  let count = 0;
  for (let i = digits.length - 1; i >= 0; i--) {
    result = digits[i] + result;
    count++;
    if (count === 3 && i !== 0) {
      result = ',' + result;
      count = 0;
    }
  }
  return result;
}

export const NumberFormatter = {
  formatResult(raw: string, useThousandsSeparator: boolean): string {
    if (!raw) return '0';
    const value = Number(raw);
    if (Number.isNaN(value) || !Number.isFinite(value)) return raw;

    const absVal = Math.abs(value);
    if (absVal >= 1e12 || (absVal > 0 && absVal < 1e-8)) {
      return raw;
    }

    if (!useThousandsSeparator) {
      if (raw.includes('.')) {
        const trimmed = raw.replace(/0+$/, '').replace(/\.$/, '');
        return trimmed || '0';
      }
      return raw;
    }

    const parts = raw.split('.');
    const integerPart = parts[0];
    const negative = integerPart.startsWith('-');
    const digits = negative ? integerPart.slice(1) : integerPart;
    const groupedInt = groupDigits(digits);
    const sign = negative ? '-' : '';

    if (parts.length > 1) {
      const frac = parts[1].replace(/0+$/, '');
      return frac ? `${sign}${groupedInt}.${frac}` : `${sign}${groupedInt}`;
    }
    return `${sign}${groupedInt}`;
  },

  formatDouble(value: number, useThousandsSeparator: boolean): string {
    if (value === 0) return '0';
    const absVal = Math.abs(value);
    if (absVal >= 1e12 || (absVal > 0 && absVal < 1e-8)) {
      return value
        .toExponential(6)
        .toUpperCase()
        .replace(/(\.\d*?[1-9])0+E/, '$1E')
        .replace(/\.0+E/, 'E')
        .replace('E+', 'E');
    }

    const fixed = Number(value.toFixed(10)).toString();
    // Handle small decimals like 1e-7 where .toString() outputs scientific notation
    const plain =
      fixed.includes('e') || fixed.includes('E')
        ? value
            .toFixed(10)
            .replace(/(\.\d*?[1-9])0+$/, '$1')
            .replace(/\.0+$/, '')
        : fixed;

    return this.formatResult(plain, useThousandsSeparator);
  },

  formatExpression(expression: string, useThousandsSeparator: boolean): string {
    if (!expression || !useThousandsSeparator) return expression;

    let result = '';
    let i = 0;
    const n = expression.length;

    while (i < n) {
      const ch = expression[i];
      if (isDigitChar(ch)) {
        const start = i;
        while (i < n && isDigitChar(expression[i])) {
          i++;
        }
        const intChunk = expression.slice(start, i);
        const prevChar = start > 0 ? expression[start - 1] : null;
        const isFractionOrExp =
          prevChar === '.' ||
          prevChar === 'E' ||
          (start >= 2 &&
            expression[start - 2] === 'E' &&
            (prevChar === '+' || prevChar === '-'));
        result += isFractionOrExp ? intChunk : groupDigits(intChunk);
      } else {
        result += ch;
        i++;
      }
    }
    return result;
  },
};
