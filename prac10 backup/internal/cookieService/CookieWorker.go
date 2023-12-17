package cookieService

import (
	"github.com/gorilla/securecookie"
	"net/http"
)

type CookieData struct {
	Data string `json:"data"`
}

type CookieWorker struct {
	cookie *securecookie.SecureCookie
	config Config
}

func NewCookieWorker(config Config) CookieWorker {
	var hashKey = securecookie.GenerateRandomKey(32)
	var blockKey = securecookie.GenerateRandomKey(16)
	return CookieWorker{
		cookie: securecookie.New(hashKey, blockKey),
		config: config,
	}
}

func (this *CookieWorker) createCookie(data CookieData) (*http.Cookie, error) {
	var encoded, err = this.cookie.Encode(this.config.cookieName, data)
	if err != nil {
		return nil, err
	}

	return &http.Cookie{
		Name:     this.config.cookieName,
		Value:    encoded,
		Path:     "/",
		HttpOnly: true,
	}, nil
}

func (this *CookieWorker) getData(name string, value string) (*CookieData, error) {
	//var cookie, err = req.Cookie(this.config.cookieName)
	//if err != nil {
	//	return nil, err
	//}
	var data CookieData
	var err = this.cookie.Decode(name, value, &data)
	return &data, err
}
