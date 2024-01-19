import { Form, FormControl } from "@angular/forms";


export interface FormData  {

    name : FormControl<string | null>,
    lastname : FormControl<string | null>,
    email : FormControl<string | null>,
    age : FormControl<number | null>,
    description : FormControl<string | null>
    
}