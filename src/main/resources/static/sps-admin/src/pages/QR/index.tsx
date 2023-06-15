import React, { useEffect } from "react";
import { useLocation, useParams } from "react-router-dom";
import QRCode from "qrcode.react";
import html2canvas from "html2canvas";

interface Space {
  id: string;
  number: string;
}

const ZoneQRCode: React.FC = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const zoneId = queryParams.get("zoneId");
  const spaces = queryParams.get("spaces");

  const [spacesList, setSpacesList] = React.useState<number[]>([]);
  const generateQRCodeContent = (spaceNumber: number) => {
    return `${zoneId}-${spaceNumber}`;
  };

  useEffect(() => {
    for (let i = 1; i <= Number(spaces); i++) {
      setSpacesList((pre) => [...pre, i]);
    }
  }, []);
  const downloadQr = (id: any) => {
    html2canvas(document.getElementById(id)!).then((canvas) => {
      const link = document.createElement("a");
      link.href = canvas.toDataURL("image/png");
      link.download = `zone-${zoneId}-${id}`; // Specify the desired filename for the downloaded image
      link.click();
    });
  };
  const downloadAll = () => {
    for (let i = 1; i <= Number(spaces); i++) {
      downloadQr(`space-${i}`);
    }
  };
  return (
    <div className="mt-10">
      <div
        className="w-1/2 md:w-1/4 p-4 m-auto shadow rounded hover:cursor-pointer"
        id="zone"
        onClick={() => downloadQr("zone")}>
        <h2 className="text-xl font-semibold mb-2 text-center">
          Zone {zoneId}
        </h2>
        <QRCode value={String(zoneId)} size={200} className="m-auto p-2" />
      </div>

      <div className="">
        <div className='flex justify-center items-center gap-5 mt-10 '>
          <h2 className="text-xl font-semibold text-center ">
            Space QR Codes
          </h2>
          <button className="float-right" onClick={downloadAll}>Download all</button>
        </div>
        <div className="bg-white p-4 rounded-lg  flex gap-20 justify-center flex-wrap">
          {spacesList.map((space) => (
            <div
              key={space}
              className="mb-4  p-4 rounded shadow hover:cursor-pointer"
              id={`space-${space}`}
              onClick={(e) => downloadQr("space-" + space)}>
              <QRCode
                value={generateQRCodeContent(space)}
                size={150}
                className="m-auto"
              />
              <p className="text-lg font-semibold mb-1 mt-7">
                Zone id : {zoneId}, Space number {space}
              </p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ZoneQRCode;
