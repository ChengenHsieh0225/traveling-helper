import "./App.css";
import TextButton from "./components/common/buttons/text-button";
import IconButton from "./components/common/buttons/icon-button";

function App() {
  return (
    <>
      <TextButton isActive="true" fontContent="現在" fontStyle="body-bold" borderStyle="semi"></TextButton>
      <TextButton isActive="true" fontContent="24 小時" fontStyle="caption" borderStyle="round"></TextButton>
      <IconButton 
        height="32px"
        isActive="true"
        borderStyle="round"
        imgSrcActive="/assets/cloud-filled.svg"
        imgSrcInactive="/assets/cloud-outlined.svg"
      >
      </IconButton>
    </>
  );
}

export default App;
