import "./Form.css";

interface FormProps extends React.FormHTMLAttributes<HTMLFormElement> {
   children: React.ReactNode;
   className?: string;
   gap?: string;
}

export default function Form({
   children,
   className = "",
   gap = "12px",
   ...props
}: FormProps) {
   return (
      <form className={`ui-form ${className}`} style={{ gap }} {...props}>
         {children}
      </form>
   );
}