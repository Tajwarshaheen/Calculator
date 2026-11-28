export type AngleMode = 'DEG' | 'RAD';

export type ThemeMode = 'DARK' | 'LIGHT' | 'SYSTEM';

export type AppRoute = 'calculator' | 'scientific' | 'history' | 'settings' | 'about';

export type CalcError =
  | 'DIVIDE_BY_ZERO'
  | 'INVALID_EXPRESSION'
  | 'MATH_DOMAIN'
  | 'OVERFLOW';

export interface HistoryItem {
  id: string;
  expression: string;
  result: string;
  rawResult: string;
  angleMode: AngleMode;
  isScientific: boolean;
  timestamp: number;
}

export interface UserPreferences {
  themeMode: ThemeMode;
  angleMode: AngleMode;
  thousandsSeparator: boolean;
  hapticFeedback: boolean;
  memoryValue: number;
}

export interface CalculatorState {
  expression: string;
  previewResult: string;
  lastEvaluatedExpression: string;
  error: CalcError | null;
  justEvaluated: boolean;
  isInverseTrig: boolean;
  preferences: UserPreferences;
  history: HistoryItem[];
}
