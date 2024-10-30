import { createContext, useState } from "react";
import 베이커리 from "../images/categoryImages/베이커리.png";
import 커피전문점 from "../images/categoryImages/커피전문점.png";
import 패스트푸드 from "../images/categoryImages/패스트푸드.png";
import 편의점 from "../images/categoryImages/편의점.png";
import 던킨 from "../images/brandImages/던킨.png";
import 뚜레쥬르 from "../images/brandImages/뚜레쥬르.png";
import 롯데리아 from "../images/brandImages/롯데리아.png";
import 맥도날드 from "../images/brandImages/맥도날드.png";
import 버거킹 from "../images/brandImages/버거킹.png";
import 빽다방 from "../images/brandImages/빽다방.png";
import 세븐일레븐 from "../images/brandImages/세븐일레븐.png";
import 스타벅스 from "../images/brandImages/스타벅스.png";
import 이마트24 from "../images/brandImages/이마트24.png";
import 투썸플레이스 from "../images/brandImages/투썸플레이스.png";
import 파리바게트 from "../images/brandImages/파리바게트.png";
import CU from "../images/brandImages/CU.png";

const Context = createContext();

const Provider = ({ children }) => {
  const [isToggled, setIsToggled] = useState(false);
  const [currentUser, setCurrentUser] = useState("");
  const [showDropDownMenu, setShowDropDownMenu] = useState(false);
  const [rating, setRating] = useState("");
  const [brandArray] = useState([
    {
      category: "커피전문점",
      categoryImage: 커피전문점,
      brand: ["빽다방", "스타벅스", "투썸플레이스"],
      brandImage: [빽다방, 스타벅스, 투썸플레이스],
      url: [
        "https://paikdabang.com/",
        "https://www.starbucks.co.kr/index.do",
        "https://www.twosome.co.kr/main.do",
      ],
    },
    {
      category: "패스트푸드",
      categoryImage: 패스트푸드,
      brand: ["롯데리아", "버거킹", "맥도날드"],
      brandImage: [롯데리아, 버거킹, 맥도날드],
      url: [
        "https://www.lotteeatz.com/brand/ria",
        "https://www.burgerking.co.kr/#/home",
        "https://www.mcdonalds.co.kr/kor/main.do",
      ],
    },
    {
      category: "편의점",
      categoryImage: 편의점,
      brand: ["CU", "세븐일레븐", "이마트24"],
      brandImage: [CU, 세븐일레븐, 이마트24],
      url: [
        "https://www.bgfretail.com/",
        "https://www.7-eleven.co.kr/",
        "https://www.emart24.co.kr/",
      ],
    },
    {
      category: "베이커리",
      categoryImage: 베이커리,
      brand: ["던킨", "파리바게트", "뚜레쥬르"],
      brandImage: [던킨, 파리바게트, 뚜레쥬르],
      url: [
        "https://www.dunkindonuts.co.kr/",
        "https://www.paris.co.kr/",
        "https://www.tlj.co.kr/index.asp",
      ],
    },
  ]);

  return (
    <Context.Provider
      value={{
        isToggled,
        setIsToggled,
        currentUser,
        setCurrentUser,
        showDropDownMenu,
        setShowDropDownMenu,
        rating,
        setRating,
        brandArray,
      }}
    >
      {children}
    </Context.Provider>
  );
};

export { Provider, Context };
