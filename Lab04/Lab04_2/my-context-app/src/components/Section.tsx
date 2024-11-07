import { ReactNode, useContext } from "react";
import { LevelContext } from "../LevelContext";

export default function Section({ children } : { children: ReactNode }) {
  const level = useContext(LevelContext);
  return (
    <section className="section">
      <LevelContext.Provider value={level + 1}>
        {children}
      </LevelContext.Provider>
    </section>
  );
}