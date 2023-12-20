const id = document.getElementById('loginUserId').value;

const eventSource = new EventSource(`http://localhost:8080/subscribe/` + id);

eventSource.addEventListener("sse", function (event) {
    console.log(event.data);
    const data = JSON.parse(event.data);

    console.log(data);

    (async () => {
        // 브라우저 알림
        const showNotification = () => {



            const notification = new Notification(data.title, {
                body: data.msg
            });

            setTimeout(() => {
                notification.close();
            }, 10 * 1000);

            notification.addEventListener('click', () => {
                window.open(data.url, '_self');
            });
        }

        // 브라우저 알림 허용 권한
        let granted = false;

        if (Notification.permission === 'granted') {
            granted = true;
        } else if (Notification.permission !== 'denied') {
            let permission = await Notification.requestPermission();
            granted = permission === 'granted';
        }

        // 알림 보여주기
        if (granted && data.newMsg) {
            showNotification();
        }
    })();
})