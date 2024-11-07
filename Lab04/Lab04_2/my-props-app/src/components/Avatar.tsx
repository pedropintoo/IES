
interface AvatarProps {
    person: {
        name: string;
        imageId: string;
    };
    size?: number;
}

export default function Avatar({ person, size = 100 }: AvatarProps) {
    return (
      <img
        className="avatar"
        src={`https://i.imgur.com/${person.imageId}.jpg`}
        alt={person.name}
        width={size}
        height={size}
      />
    );
  }
  