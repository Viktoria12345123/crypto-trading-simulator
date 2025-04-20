import React from "react";
import useForm from "../../hooks/useForm.js";
import { useAuthContext } from "../../contexts/AuthContext.js";
import { register } from "../../api/auth-api.js";
import { useNavigate } from "react-router-dom";

export default function RegisterForm() {
    const initialValues = {
        username: '',
        password: '',
        rePass: '',
    };

    const navigate = useNavigate();
    const { changeAuthState } = useAuthContext();

    const submitHandler = async (formValues) => {
        const response = await register(formValues.username, formValues.password, formValues.rePass);
        const authData = response.data

        if (authData) {
            changeAuthState({
                id: authData.id,
                username: authData.username,
            });

            navigate('/');
        }
    };

    const { values, handleChange, handleSubmit, errors } = useForm(initialValues, submitHandler);

    return (
        <div className="form-wrapper">
            <form className="form-container" onSubmit={handleSubmit}>
                <h1 className="form-title">Register</h1>

                <label>Username</label>
                <input
                    type="text"
                    name="username"
                    value={values.username}
                    onChange={handleChange}
                    className="input"
                />

                <label>Password</label>
                <input
                    type="password"
                    name="password"
                    value={values.password}
                    onChange={handleChange}
                    className="input"
                />

                <label>Confirm Password</label>
                <input
                    type="password"
                    name="rePass"
                    value={values.rePass}
                    onChange={handleChange}
                    className="input"
                />

                {errors && <p className="error-msg">{errors}</p>}

                <button type="submit" className="submit-btn">Register</button>
            </form>
        </div>
    );
}
