import { Link, type LinkProps } from "react-router-dom";
import "./Button.css";

interface Props {
   variant?: "primary" | "secondary" | "danger";
   size?: "sm" | "md" | "lg";
   className?: string;
   children: React.ReactNode;
}

export type ButtonAsButtonProps = Props &
   React.ButtonHTMLAttributes<HTMLButtonElement> & {
      to?: undefined;
   };

export type ButtonAsLinkProps = Props &
   Omit<LinkProps, "className" | "children"> & {
      to: string;
   };

export type ButtonProps = ButtonAsButtonProps | ButtonAsLinkProps;

function isLinkProps(props: ButtonProps): props is ButtonAsLinkProps {
   return typeof props.to === "string";
}

export default function Button(props: ButtonProps) {
   if (isLinkProps(props)) {
      const { to, variant = "primary", size = "md", className = "", children, ...linkProps } = props;
      const classes = `ui-button ui-button--${variant} ui-button--${size} ${className}`;
      return (
         <Link to={to} className={classes} {...linkProps}>
            {children}
         </Link>
      );
   } else {
      const { variant = "primary", size = "md", className = "", children, ...buttonProps } = props;
      const classes = `ui-button ui-button--${variant} ui-button--${size} ${className}`;
      return (
         <button className={classes} {...buttonProps}>
            {children}
         </button>
      );
   }
}