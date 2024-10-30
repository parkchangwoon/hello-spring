import style from "./styles/App.module.scss";
import "bootstrap/dist/css/bootstrap.min.css";
import { Outlet, RouterProvider, createBrowserRouter } from "react-router-dom";
import { Provider } from "./context/Context";
import Footer from "./components/Footer";
import Header from "./components/Header";
import Board from "./components/Board";
import Gallery from "./pages/Gallery";
import Home from "./pages/Home";
import Register from "./pages/Register";
import RegisterIntro from "./pages/RegisterIntro";
import Single from "./pages/Single";
import UserProfile from "./pages/UserProfile";
import Write from "./pages/Write";
import BrandAuthWrite from "./pages/BrandAuthWrite";
import HireWrite from "./pages/HireWrite";
import Message from "./pages/Message";
import MessageDetail from "./pages/MessageDetail";

const Layout = () => {
  return (
    <>
      <Header />
      <Outlet />
      <Footer />
    </>
  );
};

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      {
        path: "/",
        element: <Home />,
      },
      {
        path: "/gallery/:id",
        element: <Gallery />,
      },
      {
        path: "/single/:id",
        element: <Single />,
      },
      {
        path: "/write/:id",
        element: <Write />,
      },
      {
        path: "/userprofile/:id",
        element: <UserProfile />,
      },
      {
        path: "/board/:id",
        element: <Board />,
      },
      {
        path: "/brandauthwrite",
        element: <BrandAuthWrite />,
      },
      {
        path: "/hirewrite",
        element: <HireWrite />,
      },
      {
        path: "/message",
        element: <Message />,
      },
      {
        path: "/messagedetail/:id",
        element: <MessageDetail />,
      },
    ],
  },
  {
    path: "/registerintro",
    element: <RegisterIntro />,
  },
  {
    path: "/register/:id",
    element: <Register />,
  },
]);

function App() {
  return (
    <div className={style.App}>
      <div className={style.container}>
        <Provider>
          <RouterProvider router={router} />
        </Provider>
      </div>
    </div>
  );
}

export default App;
