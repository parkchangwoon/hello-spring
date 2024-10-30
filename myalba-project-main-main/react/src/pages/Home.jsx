import style from "../styles/Home.module.scss";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import styled from "styled-components";
import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import Slider from "react-slick";
import { Context } from "../context/Context";
import { useAxiosGet } from "../hooks/useAxiosGet";
import Card from "../components/Card";

const Home = () => {
  const { brandArray, isToggled } = useContext(Context);

  const navigate = useNavigate();

  const { data: brandRating } = useAxiosGet("http://localhost:8080/brands/top");

  const { data: popularBoard } = useAxiosGet(
    isToggled
      ? "http://localhost:8080/posts/hot/boss"
      : "http://localhost:8080/posts/hot/employee"
  );

  const { data: entireBoard } = useAxiosGet("http://localhost:8080/posts");

  const { data: hireBoard } = useAxiosGet("http://localhost:8080/hire/all");

  const settings = {
    arrow: true,
    infinite: true,
    slidesToShow: 7,
    slidesToScroll: 1,
    centerMode: true,
    centerPadding: "20px",
    autoplay: true,
    autoplaySpeed: 2000,
    nextArrow: <NextArrow />,
    prevArrow: <PrevArrow />,
  };

  return (
    <div className={style.container}>
      <div className={style.boards}>
        <div className={style.board}>
          <div className={style.boardTitle}>
            <span>인기 게시판</span>
            <span
              className={style.more}
              onClick={() =>
                navigate("/board/인기게시판", { state: { popularBoard } })
              }
            >
              더보기
            </span>
          </div>
          <div className={style.boardContent}>
            <ul>
              {popularBoard &&
                popularBoard.map((item, index) => {
                  return (
                    <li
                      key={index}
                      onClick={() => navigate(`/single/${item.id}`)}
                    >
                      {item.title}
                    </li>
                  );
                })}
            </ul>
          </div>
        </div>
        <div className={style.board}>
          <div className={style.boardTitle}>
            <span>통합 게시판</span>
            <span
              className={style.more}
              onClick={() => {
                navigate("/board/통합게시판");
              }}
            >
              더보기
            </span>
          </div>
          <div className={style.boardContent}>
            <ul>
              {entireBoard &&
                entireBoard.map((item, index) => {
                  return (
                    <li
                      key={index}
                      onClick={() => navigate(`/single/${item.id}`)}
                    >
                      {item.title}
                    </li>
                  );
                })}
            </ul>
          </div>
        </div>
        <div className={style.board}>
          <div className={style.boardTitle}>
            <span>채용공고 게시판</span>
            <span
              className={style.more}
              onClick={() => {
                navigate("/board/채용공고게시판");
              }}
            >
              더보기
            </span>
          </div>
          <div className={style.boardContent}>
            <ul>
              {hireBoard &&
                hireBoard.map((item, index) => {
                  return (
                    <li
                      key={index}
                      onClick={() => navigate(`/single/${item.id}`)}
                    >
                      {item.title}
                    </li>
                  );
                })}
            </ul>
          </div>
        </div>
        <div className={style.ratingContainer}>
          <span>브랜드 평점 랭킹</span>
          <div className={style.rating}>
            {brandRating &&
              brandRating.map((item, index) => {
                return (
                  <div key={index}>
                    {index + 1}등 {item.name} {item.averageRating}
                  </div>
                );
              })}
          </div>
        </div>
      </div>
      <div className={style.carousel}>
        <Slider {...settings}>
          {brandArray.map((item1) =>
            item1.brandImage.map((item2, index) => (
              <div className={style.imageContainer}>
                <img
                  key={index}
                  src={item2}
                  alt=""
                  onClick={() => {
                    navigate(
                      `/gallery/${isToggled ? "사장" : "알바"}${
                        item1.brand[index]
                      }`
                    );
                  }}
                />
              </div>
            ))
          )}
        </Slider>
      </div>
      <div className={style.cardContainer}>
        {brandArray.map((item, index) => {
          return <Card key={index} item={item} />;
        })}
      </div>
    </div>
  );
};

const NextArrow = styled.div.attrs((props) => ({
  currentSlide: undefined,
  slideCount: undefined,
}))`
  z-index: 1;
  background-color: transparent;
  cursor: pointer;
  &:before {
    color: black;
  }
`;

const PrevArrow = styled.div.attrs((props) => ({
  currentSlide: undefined,
  slideCount: undefined,
}))`
  z-index: 1;
  background-color: transparent;
  cursor: pointer;
  &:before {
    color: black;
  }
`;

export default Home;
