import React from "react";
import useForm from "../../hooks/useForm.js";
import {useAuthContext} from "../../contexts/AuthContext.js";
import {login} from "../../api/auth-api.js";
import {useNavigate} from "react-router-dom";

export default function LoginForm() {
    const initialValues = { username: '', password: '' };
    const navigate = useNavigate()
    const { changeAuthState } = useAuthContext();

    const submitHandler = async (formValues) => {

        const response = await login(formValues.username, formValues.password);
        const authData = response.data

        if (authData) {
            changeAuthState({
                id: authData.id,
                username: authData.username,
            })
            navigate("/home")
        }
    };

    const { values, handleChange, handleSubmit, errors } = useForm(initialValues, submitHandler);

    return (
        <div className="form-wrapper">
            <form className="form-container" onSubmit={handleSubmit}>
                <h1 className="form-title">Login</h1>

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

                {errors && <p className="error-msg">{errors}</p>}

                <button className="submit-btn">Login</button>
            </form>
        </div>
    );
}
