import type { CSSProperties } from "react";

export interface BoxProps extends React.HTMLAttributes<HTMLDivElement> {
   display?: CSSProperties["display"];
   flexDirection?: CSSProperties["flexDirection"];
   justifyContent?: CSSProperties["justifyContent"];
   alignItems?: CSSProperties["alignItems"];
   gap?: CSSProperties["gap"];
   padding?: CSSProperties["padding"];
   margin?: CSSProperties["margin"];
   width?: CSSProperties["width"];
   height?: CSSProperties["height"];
   style?: CSSProperties;
   children?: React.ReactNode;
}

export default function Box({
   display,
   flexDirection,
   justifyContent,
   alignItems,
   gap,
   padding,
   margin,
   width,
   height,
   style,
   children,
   ...props
}: BoxProps) {
   return (
      <div
         style={{
            display,
            flexDirection,
            justifyContent,
            alignItems,
            gap,
            padding,
            margin,
            width,
            height,
            ...style,
         }}
         {...props}
      >
         {children}
      </div>
   );
}