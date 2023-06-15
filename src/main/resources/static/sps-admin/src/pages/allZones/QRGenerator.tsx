import React, { useEffect, useState } from "react";
import QRCode from "qrcode";
import html2canvas from "html2canvas";
import { ZoneInfo } from "../../features/zone/api";
interface Props {
  zoneInfo: ZoneInfo;
}
// TODO
/* qr capital 
qr imagewith name and number */

const QRGenerator: React.FC<Props> = ({ zoneInfo }) => {
  const elementRef = React.useRef<HTMLDivElement>(null);
  const [qrData, setQRData] = useState<string>();
  useEffect(() => {
    QRCode.toDataURL(String(zoneInfo.id), (err, url) => {
      if (err) throw err;
      setQRData(url);
    });
  }, []);
  function downloadQRCode() {
    /* const link = document.createElement('a');
        link.download = 'QRCode.png';
        link.href = qrData!;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link); */

    html2canvas(elementRef.current!).then((canvas) => {
      const link = document.createElement("a");
      link.href = canvas.toDataURL("image/png");
      link.download = `zone-${zoneInfo.id}-qr`; // Specify the desired filename for the downloaded image
      link.click();
    });
  }
  return (
    <div className="flex justify-center flex-col">
      <div ref={elementRef} className="pb-10 w-fit m-auto">
        {qrData && (
          <img
            src={qrData}
            alt="QR Code"
            className="m-auto w-[500px] border "
          />
        )}
        <h1 className="text-center text-lg font-bold mt-2">
          Zone id : {zoneInfo.id}
        </h1>
      </div>
      <div className="flex gap-2 ">
        <button onClick={downloadQRCode} className="submit-btn w-full">
          Download QR Code for zone
        </button>
        <button onClick={downloadQRCode} className="submit-btn w-full">
          Download QR Code for each space
        </button>
      </div>
    </div>
  );
};

export default QRGenerator;
