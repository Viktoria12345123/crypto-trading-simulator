import {useNavigate} from "react-router-dom";
import {useAuthContext} from "../../contexts/AuthContext.js";

export default function  LandingPage() {

    const navigate = useNavigate();
    const { isAuthenticated } = useAuthContext()

    const handleClick = () => {

        if(isAuthenticated) {
            navigate("/home")
        } else  {
            navigate("/register")
        }
    }


    return (
        <div className="landing-container">
            <div className="message-box">
                <h1 className="title">Welcome to CryptoTracker</h1>
                <p className="subtitle">Discover the trending cryptocurrencies and make informed trades with real-time data.</p>
                <button className="cta-button" onClick={handleClick}>
                    Get Started
                </button>
            </div>
        </div>
    );
}
