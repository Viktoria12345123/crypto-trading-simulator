import { useState, useEffect } from 'react';
import {login} from "../api/auth-api.js";

const useForm = (initialValues, submitHandler) => {
    const [values, setValues] = useState(initialValues);
    const [errors, setErrors] = useState({});

    const handleChange = (e) => {
        const { name, value } = e.target;
        setValues((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log("in handler")
        console.log(values)
        setErrors({});

        try {
            await submitHandler(values);
        } catch (err) {
            setErrors(err.response?.data?.errors || { general: 'Something went wrong' });
        }
    };

    console.log(errors)

    return {
        values,
        errors,
        handleChange,
        handleSubmit,
    };
};

export default useForm;
