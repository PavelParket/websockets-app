import { Link } from "react-router-dom";
import "./Navbar.css";

export interface NavbarProps extends React.HTMLAttributes<HTMLDivElement> {
   brand?: React.ReactNode;
   rightContent?: React.ReactNode;
   brandLink?: string;
}

export default function Navbar({ brand, rightContent, brandLink = "/", className = "", ...props }: NavbarProps) {
   return (
      <nav className={`ui-navbar ${className}`} {...props}>
         <div className="ui-navbar__brand">
            {brandLink ? <Link to={brandLink} className="ui-navbar__brand-link">{brand}</Link> : brand}
         </div>
         <div className="ui-navbar__right">{rightContent}</div>
      </nav>
   );
}