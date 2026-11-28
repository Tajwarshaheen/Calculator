import { Calculator, History, Settings, Sigma } from 'lucide-react';
import type { AppRoute } from '../types/calculator';

interface NavItem {
  route: AppRoute;
  label: string;
  testId: string;
  Icon: typeof Calculator;
}

const NAV_ITEMS: NavItem[] = [
  {
    route: 'calculator',
    label: 'Calculator',
    testId: 'nav_calculator',
    Icon: Calculator,
  },
  {
    route: 'scientific',
    label: 'Scientific',
    testId: 'nav_scientific',
    Icon: Sigma,
  },
  {
    route: 'history',
    label: 'History',
    testId: 'nav_history',
    Icon: History,
  },
  {
    route: 'settings',
    label: 'Settings',
    testId: 'nav_settings',
    Icon: Settings,
  },
];

interface NavigationProps {
  currentRoute: AppRoute;
  onNavigate: (route: AppRoute) => void;
}

export function TajuBottomNavigation({
  currentRoute,
  onNavigate,
}: NavigationProps) {
  return (
    <nav
      aria-label="Primary navigation"
      className="fixed bottom-2 left-3 right-3 z-30 mx-auto max-w-[540px] rounded-[24px] border shadow-xl backdrop-blur-md md:hidden"
      style={{
        backgroundColor: 'var(--taju-surface-elevated)',
        borderColor: 'var(--taju-border)',
      }}
    >
      <div className="grid h-[64px] grid-cols-4 items-center px-1.5">
        {NAV_ITEMS.map(({ route, label, testId, Icon }) => {
          const selected =
            currentRoute === route ||
            (route === 'settings' && currentRoute === 'about');
          return (
            <button
              key={route}
              type="button"
              data-testid={testId}
              onClick={() => onNavigate(route)}
              className="flex min-h-[44px] flex-col items-center justify-center gap-1 rounded-2xl transition-colors cursor-pointer focus-visible:outline-2 focus-visible:outline-[var(--taju-gold)]"
              style={{
                color: selected ? 'var(--taju-gold)' : 'var(--taju-muted)',
              }}
            >
              <span
                className="flex items-center justify-center rounded-full px-3.5 py-1 transition-colors"
                style={{
                  backgroundColor: selected
                    ? 'rgba(193, 154, 80, 0.16)'
                    : 'transparent',
                }}
              >
                <Icon className="h-[20px] w-[20px]" strokeWidth={selected ? 2.4 : 1.9} />
              </span>
              <span
                className={`font-display text-[11px] leading-none whitespace-nowrap shrink-0 ${
                  selected ? 'font-semibold' : 'font-medium'
                }`}
              >
                {label}
              </span>
            </button>
          );
        })}
      </div>
    </nav>
  );
}

export function TajuNavigationRail({
  currentRoute,
  onNavigate,
}: NavigationProps) {
  return (
    <aside
      aria-label="Primary navigation"
      className="hidden md:flex w-[88px] shrink-0 flex-col items-center rounded-[24px] border py-4 shadow-md backdrop-blur-md"
      style={{
        backgroundColor: 'var(--taju-surface-elevated)',
        borderColor: 'var(--taju-border)',
      }}
    >
      <div className="flex w-full flex-col items-center gap-3 px-2">
        {NAV_ITEMS.map(({ route, label, testId, Icon }) => {
          const selected =
            currentRoute === route ||
            (route === 'settings' && currentRoute === 'about');
          return (
            <button
              key={route}
              type="button"
              data-testid={`${testId}_rail`}
              onClick={() => onNavigate(route)}
              className="flex w-full min-h-[54px] flex-col items-center justify-center gap-1.5 rounded-2xl py-2 transition-colors hover:bg-[var(--taju-gold)]/10 cursor-pointer focus-visible:outline-2 focus-visible:outline-[var(--taju-gold)]"
              style={{
                color: selected ? 'var(--taju-gold)' : 'var(--taju-muted)',
              }}
            >
              <span
                className="flex items-center justify-center rounded-full px-4 py-1.5 transition-colors"
                style={{
                  backgroundColor: selected
                    ? 'rgba(193, 154, 80, 0.16)'
                    : 'transparent',
                }}
              >
                <Icon className="h-[21px] w-[21px]" strokeWidth={selected ? 2.4 : 1.9} />
              </span>
              <span
                className={`font-display text-[11px] leading-none whitespace-nowrap shrink-0 ${
                  selected ? 'font-semibold' : 'font-medium'
                }`}
              >
                {label}
              </span>
            </button>
          );
        })}
      </div>
    </aside>
  );
}
