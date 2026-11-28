import { useState } from 'react';
import {
  ChevronRight,
  Compass,
  Cpu,
  Hash,
  Info,
  Moon,
  Trash2,
  Vibrate,
} from 'lucide-react';
import type { AngleMode, CalculatorState, ThemeMode } from '../types/calculator';

interface SettingsScreenProps {
  state: CalculatorState;
  formattedMemory: string;
  hasMemory: boolean;
  onSetThemeMode: (mode: ThemeMode) => void;
  onSetAngleMode: (mode: AngleMode) => void;
  onSetThousandsSeparator: (enabled: boolean) => void;
  onSetHapticFeedback: (enabled: boolean) => void;
  onResetMemory: () => void;
  onClearHistory: () => void;
  onNavigateToAbout: () => void;
}

interface OptionChipProps {
  label: string;
  selected: boolean;
  onClick: () => void;
  testId: string;
}

function OptionChip({ label, selected, onClick, testId }: OptionChipProps) {
  return (
    <button
      type="button"
      data-testid={testId}
      onClick={onClick}
      className={`font-display flex h-12 flex-1 items-center justify-center rounded-[14px] border px-3 text-[13px] transition-colors whitespace-nowrap shrink-0 cursor-pointer focus-visible:outline-2 focus-visible:outline-[var(--taju-gold)] ${
        selected ? 'font-bold' : 'font-medium'
      }`}
      style={{
        backgroundColor: selected
          ? 'rgba(193, 154, 80, 0.20)'
          : 'var(--taju-surface-elevated)',
        borderColor: selected ? 'var(--taju-gold)' : 'var(--taju-border)',
        color: selected ? 'var(--taju-gold)' : 'var(--taju-text)',
      }}
    >
      {label}
    </button>
  );
}

interface ToggleSwitchProps {
  checked: boolean;
  onChange: (checked: boolean) => void;
  label: string;
  testId: string;
}

function ToggleSwitch({ checked, onChange, label, testId }: ToggleSwitchProps) {
  return (
    <button
      type="button"
      role="switch"
      aria-checked={checked}
      aria-label={label}
      data-testid={testId}
      onClick={() => onChange(!checked)}
      className="relative inline-flex h-7 w-12 shrink-0 cursor-pointer items-center rounded-full border transition-colors focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-[var(--taju-gold)]"
      style={{
        backgroundColor: checked
          ? 'var(--taju-gold)'
          : 'var(--taju-surface-elevated)',
        borderColor: checked ? 'var(--taju-gold)' : 'var(--taju-border)',
      }}
    >
      <span
        className={`inline-block h-5 w-5 transform rounded-full transition-transform duration-150 ${
          checked ? 'translate-x-6 bg-[#07111F]' : 'translate-x-1 bg-[var(--taju-muted)]'
        }`}
      />
    </button>
  );
}

export function SettingsScreen({
  state,
  formattedMemory,
  hasMemory,
  onSetThemeMode,
  onSetAngleMode,
  onSetThousandsSeparator,
  onSetHapticFeedback,
  onResetMemory,
  onClearHistory,
  onNavigateToAbout,
}: SettingsScreenProps) {
  const [showClearConfirm, setShowClearConfirm] = useState(false);

  return (
    <div className="taju-scroll mx-auto flex h-full w-full max-w-[680px] flex-col gap-3.5 overflow-y-auto pb-6 pr-0.5">
      <div className="py-1">
        <h1 className="font-display text-[24px] font-bold tracking-[0.2px]">
          Settings
        </h1>
        <p className="text-[13px]" style={{ color: 'var(--taju-muted)' }}>
          Customize TAJU CALCULATOR preferences
        </p>
      </div>

      {/* Appearance Theme Card */}
      <section
        className="w-full rounded-[22px] border p-4"
        style={{
          backgroundColor: 'var(--taju-surface)',
          borderColor: 'var(--taju-border)',
        }}
      >
        <div className="flex items-center gap-3">
          <Moon className="h-[22px] w-[22px] shrink-0" style={{ color: 'var(--taju-gold)' }} />
          <div>
            <h2 className="text-[16px] font-semibold">Appearance Theme</h2>
            <p className="text-[13px]" style={{ color: 'var(--taju-muted)' }}>
              Switch between Luxury Navy Dark &amp; Champagne Light
            </p>
          </div>
        </div>

        <div className="mt-3.5 flex gap-2">
          <OptionChip
            label="Dark"
            selected={state.preferences.themeMode === 'DARK'}
            onClick={() => onSetThemeMode('DARK')}
            testId="theme_dark"
          />
          <OptionChip
            label="Light"
            selected={state.preferences.themeMode === 'LIGHT'}
            onClick={() => onSetThemeMode('LIGHT')}
            testId="theme_light"
          />
          <OptionChip
            label="System"
            selected={state.preferences.themeMode === 'SYSTEM'}
            onClick={() => onSetThemeMode('SYSTEM')}
            testId="theme_system"
          />
        </div>
      </section>

      {/* Angle Mode Card */}
      <section
        className="w-full rounded-[22px] border p-4"
        style={{
          backgroundColor: 'var(--taju-surface)',
          borderColor: 'var(--taju-border)',
        }}
      >
        <div className="flex items-center gap-3">
          <Compass className="h-[22px] w-[22px] shrink-0" style={{ color: 'var(--taju-gold)' }} />
          <div>
            <h2 className="text-[16px] font-semibold">Trigonometry Angle Unit</h2>
            <p className="text-[13px]" style={{ color: 'var(--taju-muted)' }}>
              Default unit for sin, cos, tan, and inverse functions
            </p>
          </div>
        </div>

        <div className="mt-3.5 flex gap-2.5">
          <OptionChip
            label="Degrees (DEG)"
            selected={state.preferences.angleMode === 'DEG'}
            onClick={() => onSetAngleMode('DEG')}
            testId="settings_angle_deg"
          />
          <OptionChip
            label="Radians (RAD)"
            selected={state.preferences.angleMode === 'RAD'}
            onClick={() => onSetAngleMode('RAD')}
            testId="settings_angle_rad"
          />
        </div>
      </section>

      {/* Formatting & Haptics Toggles */}
      <section
        className="flex w-full flex-col gap-4 rounded-[22px] border p-4"
        style={{
          backgroundColor: 'var(--taju-surface)',
          borderColor: 'var(--taju-border)',
        }}
      >
        <div className="flex items-center justify-between gap-3">
          <div className="flex items-center gap-3">
            <Hash className="h-[22px] w-[22px] shrink-0" style={{ color: 'var(--taju-gold)' }} />
            <div>
              <div className="text-[16px] font-semibold">Thousands Separator</div>
              <div className="text-[13px]" style={{ color: 'var(--taju-muted)' }}>
                {state.preferences.thousandsSeparator
                  ? 'Enabled — Preview: 1,234,567.89'
                  : 'Disabled — Preview: 1234567.89'}
              </div>
            </div>
          </div>
          <ToggleSwitch
            checked={state.preferences.thousandsSeparator}
            onChange={onSetThousandsSeparator}
            label="Thousands Separator"
            testId="switch_thousands_separator"
          />
        </div>

        <div className="flex items-center justify-between gap-3">
          <div className="flex items-center gap-3">
            <Vibrate className="h-[22px] w-[22px] shrink-0" style={{ color: 'var(--taju-gold)' }} />
            <div>
              <div className="text-[16px] font-semibold">Haptic Key Feedback</div>
              <div className="text-[13px]" style={{ color: 'var(--taju-muted)' }}>
                Tactile vibration response on keypad press
              </div>
            </div>
          </div>
          <ToggleSwitch
            checked={state.preferences.hapticFeedback}
            onChange={onSetHapticFeedback}
            label="Haptic Key Feedback"
            testId="switch_haptic_feedback"
          />
        </div>
      </section>

      {/* Storage & Memory Actions */}
      <section
        className="w-full rounded-[22px] border p-2"
        style={{
          backgroundColor: 'var(--taju-surface)',
          borderColor: 'var(--taju-border)',
        }}
      >
        <button
          type="button"
          data-testid="action_reset_memory"
          onClick={onResetMemory}
          className="flex min-h-[56px] w-full items-center justify-between rounded-[16px] px-3 py-3 text-left transition-colors hover:bg-[var(--taju-gold)]/8 cursor-pointer"
        >
          <div className="flex items-center gap-3">
            <Cpu className="h-[22px] w-[22px] shrink-0" style={{ color: 'var(--taju-gold)' }} />
            <div>
              <div className="text-[16px] font-semibold">Reset Memory Register</div>
              <div className="text-[13px]" style={{ color: 'var(--taju-muted)' }}>
                {hasMemory
                  ? `Current stored value: ${formattedMemory}`
                  : 'Memory register is empty (0)'}
              </div>
            </div>
          </div>
          <ChevronRight className="h-5 w-5 shrink-0" style={{ color: 'var(--taju-muted)' }} />
        </button>

        <button
          type="button"
          data-testid="action_clear_history"
          onClick={() => setShowClearConfirm(true)}
          className="flex min-h-[56px] w-full items-center justify-between rounded-[16px] px-3 py-3 text-left transition-colors hover:bg-[var(--taju-gold)]/8 cursor-pointer"
        >
          <div className="flex items-center gap-3">
            <Trash2 className="h-[22px] w-[22px] shrink-0" style={{ color: 'var(--taju-gold)' }} />
            <div>
              <div className="text-[16px] font-semibold">Clear Calculation History</div>
              <div className="text-[13px]" style={{ color: 'var(--taju-muted)' }}>
                {`${state.history.length} item${state.history.length === 1 ? '' : 's'} saved locally`}
              </div>
            </div>
          </div>
          <ChevronRight className="h-5 w-5 shrink-0" style={{ color: 'var(--taju-muted)' }} />
        </button>
      </section>

      {/* About TAJU Card */}
      <button
        type="button"
        data-testid="settings_about_card"
        onClick={onNavigateToAbout}
        className="flex w-full items-center justify-between rounded-[22px] border p-4 text-left transition-colors hover:brightness-105 cursor-pointer focus-visible:outline-2 focus-visible:outline-[var(--taju-gold)]"
        style={{
          backgroundColor: 'var(--taju-surface-elevated)',
          borderColor: 'var(--taju-gold-border)',
        }}
      >
        <div className="flex items-center gap-3">
          <Info className="h-6 w-6 shrink-0" style={{ color: 'var(--taju-gold)' }} />
          <div>
            <div
              className="text-[16px] font-semibold"
              style={{ color: 'var(--taju-gold)' }}
            >
              About TAJU CALCULATOR
            </div>
            <div className="text-[13px]" style={{ color: 'var(--taju-muted)' }}>
              Version 1.0.0 · Offline Precision Engine
            </div>
          </div>
        </div>
        <ChevronRight className="h-5 w-5 shrink-0" style={{ color: 'var(--taju-gold)' }} />
      </button>

      {showClearConfirm && (
        <div
          className="fixed inset-0 z-40 flex items-center justify-center bg-black/60 p-4 backdrop-blur-sm"
          role="dialog"
          aria-modal="true"
          aria-labelledby="settings-clear-history-title"
        >
          <div
            className="w-full max-w-[400px] rounded-[24px] border p-6 shadow-2xl"
            style={{
              backgroundColor: 'var(--taju-surface-elevated)',
              borderColor: 'var(--taju-border)',
            }}
          >
            <h2
              id="settings-clear-history-title"
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
                onClick={() => setShowClearConfirm(false)}
                className="font-display min-h-[40px] rounded-xl px-4 py-2 text-[14px] font-semibold transition-colors hover:bg-white/5 cursor-pointer"
                style={{ color: 'var(--taju-gold)' }}
              >
                Cancel
              </button>
              <button
                type="button"
                onClick={() => {
                  setShowClearConfirm(false);
                  onClearHistory();
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
