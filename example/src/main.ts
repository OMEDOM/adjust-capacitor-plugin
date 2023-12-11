import './style.css'
// import { AdvertisingId } from 'adjust-capacitor-plugin';
import { AdjustSDK } from 'adjust-capacitor-plugin'

// const adId = document.getElementById('txt-ad-id')!
// const adStatus = document.getElementById('txt-ad-status')!
// const btnPrompt = document.getElementById('btn-prompt-ad-tracking')!
// txt-init-status / btn-prompt-ad-tracking
const TxtInitStatus = document.getElementById('txt-init-status')!
const btnInit = document.getElementById('btn-init-btn')!

// txt-ad-status / btn-prompt-ad-tracking
const TxtAdStatus = document.getElementById('txt-ad-status')!
const btnAdTracking = document.getElementById('btn-prompt-ad-tracking')!

// txt-ad-id / btn-get-ad-id
const TxtAdId = document.getElementById('txt-ad-id')!
const btnGetAdId = document.getElementById('btn-get-ad-id')!

const initSDK = async () => {
    console.log('initSDK')
    await AdjustSDK.initSDK({
        appToken: 'qgpv5tkrnuo0',
        environment: 'sandbox',
        logLevel: 'verbose',
    })

    TxtInitStatus.innerText = 'SDK initialized'
}

const trackEvent = async () => {
    console.log('trackEvent')
    await AdjustSDK.trackEvent({
        eventToken: '2w2zwq',
    })

    TxtAdStatus.innerText = 'Event tracked'
}

const getAdId = async () => {
    console.log('getAdId')
    const adId = await AdjustSDK.getAdid()
    TxtAdId.innerText = adId.id
}

btnInit.onclick = (_: MouseEvent) => {
    initSDK();
}

btnAdTracking.onclick = (_: MouseEvent) => {
    trackEvent();
}

btnGetAdId.onclick = (_: MouseEvent) => {
    getAdId();
}
