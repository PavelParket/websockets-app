import type { ComponentPropsWithoutRef, JSX } from "react";
import "../styles/typography.css";

export function Typography(props: { variant: "h1" } & ComponentPropsWithoutRef<"h1">): JSX.Element;
export function Typography(props: { variant: "h2" } & ComponentPropsWithoutRef<"h2">): JSX.Element;
export function Typography(props: { variant: "h3" } & ComponentPropsWithoutRef<"h3">): JSX.Element;
export function Typography(props: { variant: "caption" } & ComponentPropsWithoutRef<"span">): JSX.Element;
export function Typography(props: { variant?: "body" } & ComponentPropsWithoutRef<"p">): JSX.Element;

export function Typography(
   props:
      | ({ variant: "h1" } & ComponentPropsWithoutRef<"h1">)
      | ({ variant: "h2" } & ComponentPropsWithoutRef<"h2">)
      | ({ variant: "h3" } & ComponentPropsWithoutRef<"h3">)
      | ({ variant: "caption" } & ComponentPropsWithoutRef<"span">)
      | ({ variant?: "body" } & ComponentPropsWithoutRef<"p">)
) {
   const { variant = "body", className, children, ...rest } = props;

   const Tag =
      variant === "body"
         ? "p"
         : variant === "caption"
            ? "span"
            : variant;

   return (
      <Tag
         className={`typography ${variant}${className ? ` ${className}` : ""}`}
         {...rest}
      >
         {children}
      </Tag>
   );
}
