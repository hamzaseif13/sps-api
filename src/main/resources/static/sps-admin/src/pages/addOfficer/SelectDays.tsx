import React from "react";

interface Props {
  setDays: React.Dispatch<React.SetStateAction<string[]>>;
  days: string[];
}
const SelectDays: React.FC<Props> = ({ setDays, days }) => {
  const allDays = [
   "SUNDAY",
    "MONDAY",
    "TUESDAY",
    "WEDNESDAY",
    "THURSDAY",
    "FRIDAY",
    "SATURDAY",
  ];
  const isSelected = (day: string) => {
    return days.includes(day);
  };
  const toggle = (day: string) => {
    if (days.includes(day)) {
      setDays((prevDays: string[]) => {
        return prevDays.filter((d) => d !== day);
      });
    } else {
      setDays((prevDays: string[]) => {
        return [...prevDays, day];
      });
    }
  };

  return (
    <div>
      <div className="flex gap-2 flex-wrap my-2 ">
        {allDays.map((day) => (
          <div
            className={`chip rounded-lg hover:cursor-pointer hover:bg-blue-400 ${
              isSelected(day) ? "bg-blue-500 text-white" : ""
            }`}
            onClick={() => toggle(day)}
            key={day}>
            <h1 className="font-extrabold">{day}</h1>
          </div>
        ))}
        <div
            className={`chip rounded-lg hover:cursor-pointer hover:bg-blue-400 text-white" `}
            onClick={() =>{setDays(days.length===7? []:allDays) }}
            >
            <h1 className="font-extrabold">Select All</h1>
          </div>
      </div>
    </div>
  );
};

export default SelectDays;
