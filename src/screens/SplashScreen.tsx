import { useEffect, useState } from 'react';

interface SplashScreenProps {
  onFinished: () => void;
}

export function SplashScreen({ onFinished }: SplashScreenProps) {
  const [fadingOut, setFadingOut] = useState(false);

  useEffect(() => {
    const fadeTimer = window.setTimeout(() => {
      setFadingOut(true);
    }, 1150);
    const doneTimer = window.setTimeout(() => {
      onFinished();
    }, 1450);

    return () => {
      window.clearTimeout(fadeTimer);
      window.clearTimeout(doneTimer);
    };
  }, [onFinished]);

  return (
    <div
      onClick={onFinished}
      role="presentation"
      className={`fixed inset-0 z-50 flex cursor-pointer items-center justify-center transition-opacity duration-300 ${
        fadingOut ? 'opacity-0' : 'opacity-100'
      }`}
      style={{
        background:
          'radial-gradient(circle at 50% 45%, rgba(1, 49, 70, 0.72) 0%, #07111F 72%)',
      }}
    >
      <div className="flex flex-col items-center justify-center p-8 text-center">
        {/* Rotating gold sweep ring + crest */}
        <div className="relative flex h-[164px] w-[164px] items-center justify-center">
          <svg
            viewBox="0 0 164 164"
            className="animate-taju-ring absolute inset-0 h-full w-full"
            aria-hidden="true"
          >
            <defs>
              <linearGradient id="tajuSplashGold" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" stopColor="rgba(193, 154, 80, 0)" />
                <stop offset="45%" stopColor="#C19A50" />
                <stop offset="80%" stopColor="#DFB96E" />
                <stop offset="100%" stopColor="rgba(223, 185, 110, 0)" />
              </linearGradient>
            </defs>
            <circle
              cx="82"
              cy="82"
              r="78"
              fill="none"
              stroke="url(#tajuSplashGold)"
              strokeWidth="3"
              strokeLinecap="round"
              strokeDasharray="280 210"
            />
          </svg>
          <div
            className="pointer-events-none absolute inset-[8px] rounded-full border"
            style={{ borderColor: 'rgba(193, 154, 80, 0.16)' }}
          />
          <div
            className="relative h-[126px] w-[126px] overflow-hidden rounded-full border-[1.5px] bg-[#0C1B30]"
            style={{ borderColor: 'rgba(193, 154, 80, 0.65)' }}
          >
            <img
              src="/images/img_taju_crest_1795670479652.jpg"
              alt="TAJU CALCULATOR"
              referrerPolicy="no-referrer"
              className="h-full w-full object-cover"
            />
          </div>
        </div>

        <h1 className="font-display mt-7 text-[28px] font-bold tracking-[3.2px] text-white">
          TAJU CALCULATOR
        </h1>
        <p className="font-body mt-2 text-[12px] font-semibold tracking-[2.8px] text-[#DFB96E]">
          PRECISION MATHEMATICS
        </p>
        <p className="font-body mt-6 text-[12px] text-[#98AEC9]">
          Offline Precision Engine
        </p>
      </div>
    </div>
  );
}
