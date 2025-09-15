import "./Card.css";

export interface CardProps extends React.HTMLAttributes<HTMLDivElement> {
   padding?: "sm" | "md" | "lg";
}

export default function Card({
   children,
   padding = "md",
   className = "",
   ...props
}: CardProps) {
   return (
      <div className={`ui-card ui-card--${padding} ${className}`} {...props}>
         {children}
      </div>
   );
}