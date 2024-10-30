import style from "../styles/RegisterIntro.module.scss";
import { useNavigate } from "react-router-dom";
import 알바 from "../images/logos/알바.png";
import 사장 from "../images/logos/사장.png";

const RegisterIntro = () => {
  const navigate = useNavigate();

  return (
    <div className={style.container}>
      <span className={style.mainBanner}>
        내 알바야?! 회원가입을 환영합니다.
      </span>
      <div className={style.section}>
        <img src={알바} alt="" />
        <span>아르바이트생 회원가입</span>
        <button
          className={style.employeeButton}
          onClick={() => {
            navigate("/register/아르바이트생");
          }}
        >
          회원가입
        </button>
      </div>
      <div className={style.section}>
        <img src={사장} alt="" />
        <span>자영업자 회원가입</span>
        <button
          className={style.bossButton}
          onClick={() => {
            navigate("/register/자영업자");
          }}
        >
          회원가입
        </button>
      </div>
    </div>
  );
};

export default RegisterIntro;
