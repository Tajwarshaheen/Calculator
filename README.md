# Taju Calculator

A precision basic and scientific web calculator rewritten in React, TypeScript, Tailwind CSS, and Vite.

## Features

- **Basic & Scientific Modes**: Standard arithmetic, percentages, parentheses, DEG/RAD trigonometry, inverse trig (`sin⁻¹`, `cos⁻¹`, `tan⁻¹`), logarithms (`log`, `ln`), factorials (`x!`), powers (`x²`, `x³`, `xʸ`), square roots (`√`), and mathematical constants (`π`, `e`).
- **Deterministic Expression Engine**: Offline shunting-yard / RPN evaluator with implicit multiplication, auto-closing parentheses, floating-point cleanup, and domain/zero-division error reporting.
- **Memory Registers**: `MC`, `MR`, `M+`, and `M-` persistent memory registers.
- **Calculation History**: Automatic local history persistence with one-click recall, copy-to-clipboard, individual deletion, and bulk clear.
- **Luxury Navy-and-Gold & Champagne Light Themes**: Dark, Light, and System theme modes with local preference persistence and optional thousands separators.
- **Keyboard Support**: Full physical keyboard input on Basic and Scientific views (`0-9`, `+`, `-`, `*`, `/`, `^`, `%`, `(`, `)`, `Enter`/`=`, `Backspace`, `Escape`/`Delete`).

## Development

```bash
npm install
npm run dev
```
