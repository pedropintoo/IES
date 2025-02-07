import React, { useEffect, useState } from 'react';
import logo from './logo.svg';
import './App.css';

import axios from "axios";


interface Post {
  userId?: number;
  id?: number;
  title: string;
  body: string;
}

interface FormEvent {
  preventDefault: () => void;
}

const App: React.FC = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');

  const client = axios.create({
    baseURL: "https://jsonplaceholder.typicode.com/posts" 
  });

  // GET with Axios
  useEffect(() => {
      const fetchPost = async () => {
        let response = await client.get('?_limit=5');
        setPosts(response.data);
      };
      fetchPost();
  }, []);

  // Delete with Axios
  const deletePost = async (id : number) => {
      await client.delete(`${id}`);
      setPosts(
        posts.filter((post) => {
            return post.id !== id;
        })
      );
  };

  // Post with Axios
  const addPosts = async (title: string, body: string): Promise<void> => {
      let response = await client.post<Post>('', {
        title: title,
        body: body,
      });
      setPosts((posts) => [response.data, ...posts]);
  };

  const handleSubmit = (e: FormEvent) => {
      e.preventDefault();
      addPosts(title, body);
  };

  return (
    <div>
      <h1>Posts</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        <textarea
          placeholder="Body"
          value={body}
          onChange={(e) => setBody(e.target.value)}
        />
        <button type="submit">Add Post</button>
      </form>
      <ul>
        {posts.map((post) => (
          <li key={post.id}>
            <h2>{post.title}</h2>
            <p>{post.body}</p>
            <button onClick={() => post.id !== undefined && deletePost(post.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
 };

export default App;
 