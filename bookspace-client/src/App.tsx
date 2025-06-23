import './App.css'
import {Route, Routes} from "react-router";
import BaseLayout from "@/layouts/BaseLayout.tsx";

function App() {

    return (
        <Routes>
            <Route element={<BaseLayout/>}>
                <Route index element={<div />} />
            </Route>
        </Routes>
    )
}

export default App
