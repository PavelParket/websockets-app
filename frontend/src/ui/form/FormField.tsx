import type { InputProps } from "../input/Input";
import Input from "../input/Input";
import "./FormField.css";

export interface FormFieldProps extends InputProps {
   error?: string;
}

export default function FormField({ error, ...inputProps }: FormFieldProps) {
   return (
      <div className="ui-form-field">
         <Input {...inputProps} />
         {error && <span className="ui-form-field__error">{error}</span>}
      </div>
   );
}