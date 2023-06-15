const Space = ({ space }: any) => {
  return (
      <div
      
        className={`w-20  rounded-lg h-20 ${
          space.state !== "TAKEN" ? "bg-green-400" : "bg-red-500 cursor-pointer"
        } flex justify-center items-center`}>
        <p className="text-center">{space.number}</p>
      </div>
  );
};

export default Space;
