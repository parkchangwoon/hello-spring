import styled from "styled-components";
import { useContext } from "react";
import { Context } from "../context/Context";

const Footer = () => {
  const { isToggled } = useContext(Context);

  return (
    <Div $backgroundColor={isToggled ? "skyblue" : "#FAEB78"}>
      Copyright ⓒ Myalbaya. All rights reserved.
    </Div>
  );
};

const Div = styled.span`
  display: flex;
  justify-content: center;
  background: ${(props) => props.$backgroundColor};
  padding: 50px;
  margin-top: 10px;
`;

export default Footer;
