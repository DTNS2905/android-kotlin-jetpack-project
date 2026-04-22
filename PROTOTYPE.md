# Expense Tracker — App Prototype

## Overview

A personal expense tracking Android app built with Jetpack Compose and Material Design 3.
The app helps users log daily expenses, manage categories, and stay within a monthly budget.

**Brand colors:** Primary Green `#4CAF50`, Light Green `#81C784`, Dark Green `#388E3C`
**Style:** Clean, minimal, card-based UI. Light background `#F5F5F5`, white surfaces, rounded corners (16–24dp).

---

## Screens

### 1. Home Screen

The main dashboard. Shows a greeting header with user profile picture on the right.

**Components (top to bottom):**
- **Header row** — "Welcome back," subtitle + user name in bold, circular profile avatar on the right
- **Total Card** — full-width card with horizontal green gradient, shows "Total Expenses / This month" label and a large dollar amount in white bold text
- **Budget Card** — white card below total, shows monthly budget progress bar (green fill), spent vs budget amounts, "Near limit" or "Over budget" warning badge when triggered
- **Recent Expenses section** — label "RECENT EXPENSES" with a filter icon button (badge dot when filter active) and a + FAB button inline
- **Expense list** — scrollable list of expense items, each showing category color dot, title, date, and amount

**Dialogs/Sheets:**
- Add Expense bottom dialog — title input, amount input, category picker chips
- Filter bottom sheet — time period selector (All / Today / This Week / This Month / This Year) + category filter chips

---

### 2. Search Screen

Full-screen search with a text input at the top.
Results list appears below matching the same expense item style as Home.
Empty state shown when no query or no results.

---

### 3. Settings Screen

Scrollable settings page with grouped sections.

**Sections:**
- **Profile** — shows user avatar icon + display name, tappable to edit
- **Categories** — flow row of colored category chips with × delete button each, plus an "Add Category" row below
- **Budget** — "Monthly Budget" row (shows current amount), "Budget Alert" row (shows threshold %)
- **Data** — "Clear all expenses" row in red/error color

---

### 4. Expense Detail Screen

Shows a single expense with edit mode toggle.

**Fields:** Title, Amount, Date, Category
**Actions:** Edit button in top bar, Delete button (danger style), Save button when in edit mode

---

### 5. Statistic Screen *(planned)*

Visual breakdown of spending.

**Components (planned):**
- Pie chart — spending by category
- Bar chart — daily/weekly spending trend
- Monthly summary card

---

## Navigation

Bottom navigation bar with 3 tabs:
- **Home** (house icon)
- **Search** (magnifier icon)
- **Settings** (gear icon)

Expense Detail is a full-screen push navigation (back arrow in top bar), not in the bottom nav.

---

## Component Library

| Component | Description |
|---|---|
| `TotalCard` | Green gradient card, white text, large amount display |
| `BudgetCard` | White card, linear progress bar, warning states |
| `ExpenseItem` | Row with color dot, title, date subtitle, amount |
| `CategoryChip` | Pill-shaped chip with category color |
| `AppButton` | PRIMARY (green filled), SECONDARY (outlined), DANGER (red), GHOST (text only) |
| `FloatingCard` | Animated toast at top of screen — SUCCESS (green), ERROR (red), WARNING (amber), INFO (blue) |
| `CustomDialog` | Centered modal with title, content slot, confirm/dismiss buttons |

---

## Image Prompts

Use the prompts below to generate UI mockup images.

---

### Prompt 1 — Home Screen

```
Design a mobile app screen for an Android expense tracker called "Expense Tracker".
Light theme, Material Design 3 style.

Layout (top to bottom, portrait phone):
- Status bar at the top
- Header row: left side shows small gray text "Welcome back," above bold black text "Sang". Right side has a small circular profile photo
- A wide card with a left-to-right green gradient (#4CAF50 to #81C784), rounded corners 16dp, white text. Top shows "Total Expenses" in medium weight and "This month" in small faded text below it. Large bold white text "$1,240.00" underneath
- A white card below with a green progress bar at ~62% fill, left label "Spent $1,240" and right label "Budget $2,000", small green badge "62% used"
- Section label "RECENT EXPENSES" in small caps gray text, with a filter icon and a circular + button on the right
- A scrollable list of 4-5 expense rows. Each row has a small colored circle on the left (green, blue, orange, red), expense title in black, small gray date below, and bold amount on the right
- Bottom navigation bar with 3 icons: Home (selected, green), Search, Settings

Background: light gray #F5F5F5. Card shadows subtle. Font clean sans-serif.
```

---

### Prompt 2 — Home Screen with Budget Warning

```
Design a mobile app screen for an Android expense tracker.
Light theme, Material Design 3. Same layout as the home screen but with a budget warning state.

The budget card shows a nearly-full amber/orange progress bar at ~85% fill.
A small amber warning badge reads "Near limit!" on the budget card.
A floating notification card appears at the very top of the screen (below the status bar) with an amber/yellow background, warning icon, and text "You are near your budget limit".

Everything else identical to the normal home screen.
```

---

### Prompt 3 — Add Expense Dialog

```
Design a mobile app bottom sheet dialog for adding a new expense in an Android app.
Light theme, Material Design 3.

The bottom sheet rises from the bottom, rounded top corners 24dp, white background.
A drag handle at the top center.
Title "Add Expense" in bold.

Form fields (top to bottom):
- Text input labeled "Title" with placeholder "e.g. Coffee"
- Text input labeled "Amount" with a "$" prefix, numeric keyboard
- A row of small pill-shaped category chips below: "Food" (green), "Transport" (blue), "Shopping" (orange), "Health" (red), one chip is selected with a filled color

At the bottom: a full-width green filled button "Add Expense" and a ghost text button "Cancel" below it.

Background behind the sheet is dimmed dark overlay.
```

---

### Prompt 4 — Settings Screen

```
Design a mobile app settings screen for an Android expense tracker.
Light theme, Material Design 3, scrollable page.

Top: large bold text "Settings"

Section 1 — "Profile":
- A list item row with a person icon on the left, bold text "Sang", subtitle "Edit profile", chevron arrow on right

Section 2 — "Categories":
- A white rounded card containing a flow of colored pill-shaped chips: "Food" (green), "Transport" (blue), "Shopping" (orange), "Health" (red), each with a small × icon
- Below the card, a list row with a + icon and text "Add Category"

Section 3 — "Budget":
- List row: wallet icon, "Monthly Budget", subtitle "$2,000.00", chevron right
- List row: bell icon, "Budget Alert", subtitle "80%", chevron right

Section 4 — "Data":
- List row: trash icon in red, "Clear all expenses" text in red, no chevron

Bottom navigation bar: Home, Search, Settings (Settings tab active/selected).
Background light gray. Section headers in small caps gray. Cards with subtle shadow.
```

---

### Prompt 5 — Expense Detail Screen

```
Design a mobile app detail screen for viewing and editing a single expense in an Android tracker app.
Light theme, Material Design 3.

Top app bar: back arrow on left, title "Expense Detail", "Edit" text button on right (green).

Content (centered card layout):
- Large white card with rounded corners 16dp, padding 24dp
- Row: label "Title" gray small text, value "Morning Coffee" black bold below
- Row: label "Amount" gray small, value "$12.50" large green bold
- Row: label "Date" gray small, value "22 Apr 2026" black
- Row: label "Category" gray small, a small green pill chip "Food" as value

Below the card: a full-width red outlined button with trash icon "Delete Expense"

Clean white background, subtle card shadow, standard Android top bar.
```

---

### Prompt 6 — Statistic Screen (Planned)

```
Design a mobile app statistics screen for an Android expense tracker.
Light theme, Material Design 3.

Top: bold title "Statistics", a month selector row showing "< April 2026 >" centered

Content (top to bottom):
- A white card with a donut/pie chart showing spending by category. Colored segments: green (Food 40%), blue (Transport 25%), orange (Shopping 20%), red (Health 15%). Legend below the chart with colored dots and labels + percentages
- A white card below with a bar chart showing daily spending for the past 7 days. Bars in primary green, x-axis shows day abbreviations (Mon–Sun)
- A summary card: total spent this month "$1,240.00", number of transactions "23", average per day "$41.33"

Bottom navigation bar: Home, Search, Settings.
```
