import React, { useEffect, useState } from 'react';
import './App.css';

interface Post {
  userId?: number;
  id?: number;
  title: string;
  body: string;
}

interface FormEvent {
  preventDefault: () => void;
}

const AppFetch = () => {
	const [posts, setPosts] = useState<Post[]>([]);
	const [title, setTitle] = useState('');
	const [body, setBody] = useState('');

	// GET with fetch API
	useEffect(() => {
		const fetchPost = async () => {
			const response = await fetch(
					'https://jsonplaceholder.typicode.com/posts?_limit=5'
			);
			const data = await response.json();
			console.log(data);
			setPosts(data);
		};
		fetchPost();
	}, []);

	// Delete with fetchAPI
	const deletePost = async (id : number) => {
		let response = await fetch(
			`https://jsonplaceholder.typicode.com/posts/${id}`,
			{
					method: 'DELETE',
			}
		);
		if (response.status === 200) {
			setPosts(
					posts.filter((post) => {
						return post.id !== id;
					})
			);
		} else {
			return;
		}
		
	};

	// Post with fetchAPI
	interface AddPostResponse {
		id: number;
		title: string;
		body: string;
		userId: number;
	}

	const addPosts = async (title: string, body: string): Promise<void> => {
		let response = await fetch('https://jsonplaceholder.typicode.com/posts', {
			method: 'POST',
			body: JSON.stringify({
				title: title,
				body: body,
				userId: Math.random().toString(36).slice(2),
			}),
			headers: {
				'Content-type': 'application/json; charset=UTF-8',
			},
		});
		let data: AddPostResponse = await response.json();
		setPosts((posts) => [data, ...posts]);
		setTitle('');
		setBody('');
	};

	const handleSubmit = (e : FormEvent) => {
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

export default AppFetch;
