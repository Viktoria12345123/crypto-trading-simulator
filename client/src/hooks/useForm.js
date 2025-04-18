import { useState, useEffect } from 'react';

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
