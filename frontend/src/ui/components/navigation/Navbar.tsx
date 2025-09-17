import type { HTMLAttributes, ReactNode } from "react";
import { Container } from "../layout/Container";

type NavbarProps = HTMLAttributes<HTMLElement> & {
   brand?: ReactNode;
   right?: ReactNode;
};

export function Navbar({ brand, right, style, ...rest }: NavbarProps) {
   return (
      <header
         style={{
            position: "sticky",
            top: 0,
            zIndex: 100,
            background: "var(--glass-surface)",
            backdropFilter: `blur(var(--glass-blur))`,
            boxShadow: "var(--shadow-sm)",
            ...style,
         }}
         {...rest}
      >
         <Container>
            <div style={{
               display: "flex",
               alignItems: "center",
               justifyContent: "space-between",
               height: "64px",
            }}>
               <div>{brand}</div>
               <nav style={{ display: "flex", alignItems: "center", gap: "0.5rem" }}>
                  {right}
               </nav>
            </div>
         </Container>
      </header>
   );
}
