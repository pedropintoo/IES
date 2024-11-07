import React from 'react';
import logo from './logo.svg';
import './App.css';
import Profile from './components/Profile';
import ShoppingList from './components/ShoppingList';
import { useState } from 'react';


interface MyButtonProps {
  count: number;
  onClick: () => void;
}

function MyButton({ count, onClick }: MyButtonProps) {

  return (
    <button onClick={onClick}>
      Clicked {count} times
    </button>
  );
}

function AboutPage() {
  return (
    <>
      <h1>About</h1>
      <p>Hello there.<br />How do you do?</p>
    </>
  );
}

function MyApp() {
  const [count, setCount] = useState(0);

  function handleClick() {
    setCount(count + 1);
  }

  return (
    <>
      <img className="avatar" />
      <AboutPage />
      <div>
        <h1>Welcome to my app</h1>
        <div>
          <h1>Counters that update together</h1>
          <MyButton count={count} onClick={handleClick} />
          <MyButton count={count} onClick={handleClick} />
        </div>
      </div>
      <Profile />
      <ShoppingList />
    </>
  );
}

export default MyApp;
