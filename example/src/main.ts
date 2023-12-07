import './style.css'
// import { AdvertisingId } from 'adjust-capacitor-plugin';

const adId = document.getElementById('txt-ad-id')!
const adStatus = document.getElementById('txt-ad-status')!
const btnPrompt = document.getElementById('btn-prompt-ad-tracking')!

const prompt = async () => {
  adStatus.innerText = "adStatus"
  adId.innerText = "adId";
}

btnPrompt.onclick = (_: MouseEvent) => {
  prompt();
}
