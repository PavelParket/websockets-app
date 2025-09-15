import "./Input.css";

export interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
   fullWidth?: boolean;
}

export default function Input({
   className = "",
   fullWidth = false,
   ...props
}: InputProps) {
   return (
      <input
         className={`ui-input ${fullWidth ? "ui-input--full" : ""} ${className}`}
         {...props}
      />
   );
}