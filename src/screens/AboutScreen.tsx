import { ArrowLeft, Calculator, FlaskConical, Lock, Zap } from 'lucide-react';
import type { LucideIcon } from 'lucide-react';

interface AboutScreenProps {
  onBack: () => void;
}

interface HighlightCardProps {
  Icon: LucideIcon;
  title: string;
  description: string;
}

function HighlightCard({ Icon, title, description }: HighlightCardProps) {
  return (
    <div
      className="flex w-full items-start gap-3.5 rounded-[20px] border p-4"
      style={{
        backgroundColor: 'var(--taju-surface)',
        borderColor: 'var(--taju-border)',
      }}
    >
      <div
        className="flex h-[42px] w-[42px] shrink-0 items-center justify-center rounded-[12px]"
        style={{
          backgroundColor: 'rgba(193, 154, 80, 0.14)',
          color: 'var(--taju-gold)',
        }}
      >
        <Icon className="h-[22px] w-[22px]" />
      </div>
      <div>
        <h3 className="text-[16px] font-semibold">{title}</h3>
        <p className="mt-1 text-[13px] leading-relaxed" style={{ color: 'var(--taju-muted)' }}>
          {description}
        </p>
      </div>
    </div>
  );
}

export function AboutScreen({ onBack }: AboutScreenProps) {
  return (
    <div className="taju-scroll mx-auto flex h-full w-full max-w-[680px] flex-col gap-3.5 overflow-y-auto pb-6 pr-0.5">
      {/* Top Bar with Back Arrow */}
      <div className="flex items-center gap-1.5">
        <button
          type="button"
          data-testid="about_back_button"
          onClick={onBack}
          aria-label="Back to Settings"
          className="flex h-12 w-12 items-center justify-center rounded-xl transition-colors hover:bg-[var(--taju-gold)]/12 cursor-pointer focus-visible:outline-2 focus-visible:outline-[var(--taju-gold)]"
          style={{ color: 'var(--taju-gold)' }}
        >
          <ArrowLeft className="h-6 w-6" />
        </button>
        <h1 className="font-display text-[24px] font-bold">About TAJU</h1>
      </div>

      {/* Brand Crest Hero Card */}
      <div
        className="flex w-full flex-col items-center rounded-[26px] border p-6 text-center"
        style={{
          backgroundColor: 'var(--taju-surface-elevated)',
          borderColor: 'var(--taju-gold-border)',
        }}
      >
        <div
          className="h-24 w-24 overflow-hidden rounded-full border-2 bg-[#0C1B30]"
          style={{ borderColor: 'var(--taju-gold)' }}
        >
          <img
            src="/images/img_taju_crest_1795670479652.jpg"
            alt="TAJU CALCULATOR"
            referrerPolicy="no-referrer"
            className="h-full w-full object-cover"
          />
        </div>

        <h2
          className="font-display mt-4 text-[24px] font-bold tracking-[2.2px]"
          style={{ color: 'var(--taju-gold-bright)' }}
        >
          TAJU CALCULATOR
        </h2>
        <p className="mt-1 text-[13px]" style={{ color: 'var(--taju-muted)' }}>
          Version 1.0.0 · Offline Precision Edition
        </p>
        <p className="mt-3.5 max-w-[520px] text-[15px] leading-relaxed">
          Crafted for fast, reliable everyday and scientific calculations with a
          calm navy-and-gold glass interface and full offline privacy.
        </p>
      </div>

      {/* Capabilities List */}
      <HighlightCard
        Icon={Calculator}
        title="Deterministic Expression Parser"
        description="Evaluates nested brackets, implicit multiplication, and mathematical operator precedence safely without dynamic script evaluation."
      />
      <HighlightCard
        Icon={FlaskConical}
        title="Full Scientific Suite"
        description="Supports DEG/RAD trigonometry, inverse trig functions, logarithms, factorials, powers, roots, π, e, and persistent memory registers."
      />
      <HighlightCard
        Icon={Zap}
        title="100% Offline & Lightweight"
        description="Instant zero-latency arithmetic and local browser history storage—no internet connection or account required."
      />
      <HighlightCard
        Icon={Lock}
        title="Private Local Storage"
        description="Your calculation history, memory values, and theme preferences remain stored exclusively on your device."
      />
    </div>
  );
}
