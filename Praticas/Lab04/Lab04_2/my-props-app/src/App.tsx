
import Avatar from './components/Avatar';
import Card from './components/Card';
import MovingDot from './components/MovingDot';


export default function Profile() {
  return (
    <>
      
      <Card>
        <Avatar
          size={100}
          person={{ 
            name: 'Katsuko Saruhashi',
            imageId: 'YfeOqp2'
          }}
        />
      </Card>
      <MovingDot />
    </>
  );
}

