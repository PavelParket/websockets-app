import { useTheme } from "../theme/useTheme";
import { Button } from "./Button";

export function ThemeSwitcher() {
   const { theme, toggleTheme } = useTheme();

   return (
      <Button variant="ghost" onClick={toggleTheme}>
         {theme === "light" ? "🌙 Dark Mode" : "☀️ Light Mode"}
      </Button>
   );
}