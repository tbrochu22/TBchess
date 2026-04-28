# Chess Demo — an OOP / Swing Walkthrough

A demo that draws a chess board, lets you click-to-select and
click-to-move pieces, and delegates rule-checking to a placeholder
component. Pieces are rendered in an 80s arcade "big pixel" style.

```
javac *.java
java ChessDemo
```

---

## 1. AWT vs Swing — one-paragraph refresher

**AWT** (`java.awt.*`, 1995) is the original GUI toolkit. Every AWT
widget is a *heavyweight* component backed by a native OS control
(a real Windows/macOS/X11 window handle). AWT gives you the low-level
types we still use every day — `Graphics`, `Graphics2D`, `Color`,
`Font`, `Rectangle`, `MouseEvent`, `KeyEvent` — plus the *event
dispatch* machinery.

**Swing** (`javax.swing.*`, 1998) is a second layer built *on top of*
AWT. Swing widgets are *lightweight*: there is exactly one native
window (the `JFrame`), and everything inside it — buttons, panels,
trees — is just Java code drawing itself with `Graphics2D`. That's why
Swing looks identical on every OS and why we can paint our own chess
pieces: we already *are* the rendering engine.

Rule of thumb for this class:
- Use **Swing** for anything you put on screen (`JFrame`, `JPanel`, `JButton`).
- Use **AWT** for the primitives those Swing classes hand you
  (`Graphics2D`, `Color`, `MouseEvent`).

---

## 2. The class hierarchy behind `ChessBoardPanel`

```
java.lang.Object
   └── java.awt.Component           ← has location, size, paint(), event dispatch
          └── java.awt.Container    ← can hold child components
                 └── javax.swing.JComponent
                        └── javax.swing.JPanel
                               └── ChessBoardPanel   (ours)
```

Every `JPanel` is-a `Container` is-a `Component`. By `extends JPanel`
we inherit *hundreds* of methods — `repaint()`, `addMouseListener()`,
`setPreferredSize()` — and we override only the two that matter:
`paintComponent(Graphics)` and (implicitly, via our adapter) the
mouse callbacks.

This is the single most important OOP fact in Swing: **you customise
behaviour by subclassing and overriding, not by configuration flags.**

---

## 3. Two pipelines you must understand

### (a) The paint pipeline (template-method pattern)

1. Something calls `panel.repaint()`.
2. Swing schedules a paint job on the **Event Dispatch Thread (EDT)**.
3. The EDT calls `paint(Graphics)`, which calls
   `paintComponent(Graphics)` on our panel.
4. We get a ready-made `Graphics2D` object (from AWT) and draw squares,
   sprites, and the selection ring.

We never call `paintComponent` ourselves. Swing calls *us*. That's a
textbook **template method**: the framework fixes the algorithm, we
override a hook.

### (b) The event pipeline (observer pattern)

```
OS mouse click
   → native window → AWT event queue
      → EDT dequeues → fires MouseEvent
         → every registered MouseListener on the panel
            → our MouseAdapter.mouseClicked(e)
               → handleClick(x, y) mutates state, calls repaint()
```

`addMouseListener(new MouseAdapter() { ... })` is the **observer**
registration. Our anonymous inner class is a one-off *subject-to-observer
subscription*. When the subject (the panel) detects a click, it notifies
every observer it knows about.

---

## 4. File-by-file tour

| File | Role | OOP concept |
|------|------|-------------|
| `Piece.java` | Immutable value holder: `Type`, `Color`. | **Encapsulation** (final fields, read-only getters). **Enums** as first-class types — no stringly-typed bugs. |
| `ChessRules.java` | Asks "is this move legal?" given the whole board. | **Delegation / Strategy**. The panel does not know the rules; it asks this collaborator. Swap the class to swap the rules. |
| `PixelSpriteRenderer.java` | Turns a `Piece` + position into pixels using 8×8 bitmaps. | **Single responsibility**: nothing here knows about mouse, state, or board layout. Pure render. |
| `ChessBoardPanel.java` | The `JPanel`. Owns `Piece[][] board`, `ChessRules`, `PixelSpriteRenderer`. Runs the click state machine. Paints. | **Composition** ("has-a" each collaborator), **inheritance** (extends `JPanel`), **polymorphism** (overrides `paintComponent`). |
| `ChessDemo.java` | `main` — build a `JFrame`, put the panel in it, show it. | **Separation of construction from behaviour**: the demo class never touches game state. |

### How they collaborate in one click

```
user clicks (x,y)
   ChessBoardPanel.mouseClicked
      handleClick
         if nothing selected:   remember (row,col)
         else:                   rules.isLegalMove(board, from, to)
                                    └─ ChessRules decides
                                 if legal: board[to]=board[from]; board[from]=null
         repaint()
   Swing schedules paint
   paintComponent(g)
      for each square: g.fillRect(...)       ← AWT primitives
      for each piece : sprites.draw(g, ...)  ← delegates to PixelSpriteRenderer
      if selected    : g.drawRect(...)       ← neon cyan ring
```

Every arrow above is an **object sending a message** (method call) to
another object it already holds a reference to. That is OOP.

---

## 5. OOP concepts in action

- **Encapsulation** — `Piece` exposes `getType()` / `getColor()` only;
  its fields are `private final`. The board array in `ChessBoardPanel`
  is `private`; nothing outside can mutate it.
- **Composition over inheritance** — `ChessBoardPanel` *has-a*
  `ChessRules` and *has-a* `PixelSpriteRenderer`. It does not extend
  them. Swap either collaborator without touching the panel.
- **Inheritance** — `ChessBoardPanel extends JPanel`. We reuse Swing's
  entire painting + event infrastructure for free.
- **Polymorphism** — Swing calls `paintComponent(Graphics)` on "some
  JPanel"; at runtime our override runs. Same mechanism lets
  `MouseAdapter` pretend to be a `MouseListener`.
- **Delegation / Strategy** — legality lives in `ChessRules`, not the
  panel. Give me a `CheckersRules` with the same method and the panel
  becomes a checkers board.
- **Observer** — `addMouseListener` / `addMouseMotionListener`.
- **Template method** — `paint` → `paintComponent` (we override only
  the hook).
- **Enums as types** — `Piece.Type`, `Piece.Color` used in a
  `switch`-like map inside `PixelSpriteRenderer`.

---

## 6. Things to do 

1. **New skin.** Create `FlatSpriteRenderer` with the same `draw(...)`
   signature. Add a constructor parameter on `ChessBoardPanel` that
   takes *any* renderer object.

2. **Captured-pieces sidebar.** Add a third `JPanel` next to the
   board in `ChessDemo` using a `BorderLayout`.
