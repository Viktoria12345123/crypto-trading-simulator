import {useNavigate} from "react-router-dom";

export default function  LandingPage() {

    const navigate = useNavigate();


    return (
        <div className="landing-container">
            <div className="message-box">
                <h1 className="title">Welcome to CryptoTracker</h1>
                <p className="subtitle">Discover the trending cryptocurrencies and make informed trades with real-time data.</p>
                <button className="cta-button" onClick={()=> navigate("/login")}>
                    Get Started
                </button>
            </div>
        </div>
    );
}
