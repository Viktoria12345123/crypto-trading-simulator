import { useState } from 'react';

const useForm = (initialValues, submitHandler) => {
    const [values, setValues] = useState(initialValues);
    const [errors, setErrors] = useState('');

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
            setErrors(err.response?.data ||  'Something went wrong' );
        }
    };

    return {
        values,
        errors,
        handleChange,
        handleSubmit,
    };
};

export default useForm;
